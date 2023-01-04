package com.ayoam.orderservice.service;

import com.ayoam.orderservice.converter.OrderConverter;
import com.ayoam.orderservice.dto.*;
import com.ayoam.orderservice.kafka.publisher.OrderPublisher;
import com.ayoam.orderservice.model.Invoice;
import com.ayoam.orderservice.model.Order;
import com.ayoam.orderservice.model.OrderStatus;
import com.ayoam.orderservice.repository.OrderRepository;
import com.ayoam.orderservice.repository.OrderStatusRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {
    private OrderRepository orderRepository;
    private OrderConverter orderConverter;
    private OrderStatusRepository orderStatusRepository;
    private WebClient.Builder webClientBuiler;
    private OrderPublisher orderPublisher;

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderConverter orderConverter, OrderStatusRepository orderStatusRepository, WebClient.Builder webClientBuiler, OrderPublisher orderPublisher) {
        this.orderRepository = orderRepository;
        this.orderConverter = orderConverter;
        this.orderStatusRepository = orderStatusRepository;
        this.webClientBuiler = webClientBuiler;
        this.orderPublisher = orderPublisher;
    }

    public Order placeOrder(OrderDto orderDto) {
        Order order = orderConverter.orderDtoToOrder(orderDto);
        OrderStatus status = orderStatusRepository.findById(orderDto.getStatusId()).orElse(null);
        if(status==null){
            throw new RuntimeException("Order status not found!");
        }
        order.setStatus(status);
        Invoice invoice = new Invoice();
        order.setInvoice(invoice);
        order.calculateOrderTotal();
        //orderRequest list
//        List<OrderRequest> orderRequestList = new ArrayList<>();
//        for(OrderLineItem item:orderDto.getOrderLineItemList()){
//            OrderRequest orderRequest = new OrderRequest();
//            orderRequest.setProductId(item.getProductId());
//            orderRequest.setQuantity(item.getQuantity());
//            orderRequestList.add(orderRequest);
//        }

        //check inventory in inventory service ==> WebClient
//        InventoryResponse[] result = new InventoryResponse[0];
//        result = webClientBuiler.build().post()
//                .uri("http://localhost:8080/api/v1/inventories/checkInventory")
//                .body(Mono.just(orderRequestList),new ParameterizedTypeReference<List<OrderRequest>>() {})
//                .retrieve()
//                .bodyToMono(InventoryResponse[].class)
//                .block();
//
//        Boolean allItemsAvailable = (Arrays.stream(result).allMatch(InventoryResponse::getInStock));
//        if(!allItemsAvailable){
//            throw new RuntimeException("some items are not available at the moment!");
//        }


        //send order placed event to inventory service ==> Kafka
//        OrderPlacedEvent event= new OrderPlacedEvent();
//        event.setOrderRequestList(orderRequestList);
//        orderPublisher.placeOrderEvent(event);

//        return null;

        //clear Customer's cart
        try {
            webClientBuiler.build().post()
                    .uri("http://localhost:8080/api/v1/carts/"+orderDto.getCartID()+"/clear")
                    .retrieve()
                    .toEntity(JsonNode.class)
                    .block();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return orderRepository.save(order);
    }

    public Order updateOrderStatus(OrderStatusRequest orderStatus, Long id) {
        Order order = orderRepository.findById(id).orElse(null);
        if(order==null){
            throw new RuntimeException("Order not found!");
        }
        OrderStatus status = orderStatusRepository.findById(orderStatus.getOrderStatusId()).orElse(null);
        if(status==null){
            throw new RuntimeException("Order status not found!");
        }
        order.setStatus(status);
        return orderRepository.save(order);
    }

    public OrdersListResponse getAllOrders(Map<String,String> filters) {
        OrdersListResponse res = new OrdersListResponse();
        Pageable pages = PageRequest.of(Integer.parseInt(filters.get("page")),Integer.parseInt(filters.get("limit")), Sort.by(Sort.Direction.DESC, "orderNumber"));
        res.setOrderList(orderRepository.findAll(pages).getContent());
        return res;
    }

    public OrdersListResponse getOrdersByCustomerId(Long customerId) {
        List<Order> orderList = orderRepository.findByCustomerIDOrderByOrderNumberDesc(customerId).orElse(new ArrayList<Order>());
        return new OrdersListResponse(orderList);
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow();
    }

    public byte[] generateInvoice(HttpServletResponse servletResponse ,Long invoiceId) throws FileNotFoundException, JsonProcessingException {
        InvoiceGenRequest request = new InvoiceGenRequest();

        Order order = orderRepository.findByInvoiceInvoiceId(invoiceId).orElse(null);

        if(order==null){
            throw new RuntimeException("invoiceID invalid!");
        }

        List<InvoiceItem> invoiceItems = order.getOrderLineItemList().stream().map(item->new InvoiceItem(item.getLibelle(),item.getQuantity(),item.getPrice())).toList();
        request.setItems(invoiceItems);
        request.setLogo("https://i.ibb.co/KDng43T/logo.png");
        request.setFrom("Moodluxe \n Leatherwood Rd. \n Livermore, CA 94550 \n United states");
        request.setTo(order.getOrderAdresse().getFormattedAdresse());
        request.setNumber(invoiceId);
        request.setDate(order.getInvoice().getFormattedInvoiceDate());
        request.setAmount_paid(order.getOrderTotal());
        request.setNotes("Thank you for shopping with us!");
        request.setDate_title("Invoice date");

        servletResponse.setContentType("application/pdf");
        servletResponse.setHeader("Content-Disposition","attachment; filename=\"Invoice #"+invoiceId+".pdf\";");

        try {
            byte[] response = webClientBuiler.build().post()
                    .uri("https://invoice-generator.com")
                    .body(Mono.just(request), InvoiceGenRequest.class)
                    .retrieve()
                    .bodyToMono(ByteArrayResource.class)
                    .map(ByteArrayResource::getByteArray)
                    .block();
            return response;
        } catch (WebClientResponseException ex) {
            throw new RuntimeException();
        }
    }

    public SalesStatisticsResponse getSalesStatistics() {
        SalesStatisticsResponse response = new SalesStatisticsResponse();
        List<SalesStatistics> salesStatistics = orderRepository.getSalesStatistics();
        for(int i=1;i<=12;i++){
            if(!salesStatistics.stream().map(SalesStatistics::getMonth).toList().contains(i)){
                salesStatistics.add(new SalesStatistics(i,0D));
            }
        }
        salesStatistics.sort(Comparator.comparing(SalesStatistics::getMonth));
        response.setSalesPerMonth(salesStatistics);
        return response;
    }

    public TotalOrdersAndSalesResponse getTotalOrdersAndSales() {
        return orderRepository.getTotalOrdersAndSales();
    }
}
