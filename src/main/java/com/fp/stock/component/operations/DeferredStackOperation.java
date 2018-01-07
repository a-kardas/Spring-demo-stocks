package com.fp.stock.component.operations;

import com.fp.stock.dto.StockDTO;
import com.fp.stock.model.User;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

import java.security.Principal;

@Data
public class DeferredStackOperation extends DeferredResult<ResponseEntity<?>> {

    private User user;

    private StockDTO stockDTO;

    private OperationType type;


}
