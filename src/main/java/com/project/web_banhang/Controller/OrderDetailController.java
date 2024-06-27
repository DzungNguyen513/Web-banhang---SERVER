package com.project.web_banhang.Controller;

import com.project.web_banhang.Components.LocalizationUtils;
import com.project.web_banhang.DTOS.OrderDTO;
import com.project.web_banhang.DTOS.OrderDetailDTO;
import com.project.web_banhang.Exceptions.DataNotFoundException;
import com.project.web_banhang.Model.OrderDetail;
import com.project.web_banhang.Responses.OrderDetailResponse;
import com.project.web_banhang.Service.IOrderDetailService;
import com.project.web_banhang.Service.OrderDetailService;
import com.project.web_banhang.Utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/order_details")
public class OrderDetailController {

    private final IOrderDetailService orderDetailService;
    private final LocalizationUtils localizationUtils;


    @PostMapping("")
    public ResponseEntity<?> createOrderDetail (@Valid @RequestBody OrderDetailDTO orderDetailDTO) {
        try {
            OrderDetail newOrderDetail = orderDetailService.createOrderDetail(orderDetailDTO);
            return ResponseEntity.ok(OrderDetailResponse.fromOrderDetail(newOrderDetail));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetailById (@Valid @PathVariable("id") long id) throws DataNotFoundException {
        OrderDetail orderDetail = orderDetailService.getOrderDetail(id);
        return ResponseEntity.ok(orderDetail);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getOrderDetailByOrderId(@Valid @PathVariable("orderId") long orderId) {
        List<OrderDetail> orderDetails = orderDetailService.findByOrderId(orderId);
        List<OrderDetailResponse> orderDetailResponses = orderDetails.stream().map(OrderDetailResponse :: fromOrderDetail).toList();
        return ResponseEntity.ok(orderDetailResponses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderDetail(@Valid @PathVariable("id") long id, @Valid @RequestBody OrderDetailDTO orderDetailDTO) {
        try {
            OrderDetail orderDetail = orderDetailService.updateOrderDetail(id, orderDetailDTO);
            return ResponseEntity.ok().body(orderDetail);

        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderDetail(@Valid @PathVariable("id") long id) {
        orderDetailService.deleteOrderDetail(id);
        return ResponseEntity.ok().body(localizationUtils.getLocalizeMessage(MessageKeys.DELETE_ORDER_DETAIL_SUCCESSFULLY));


    }

}
