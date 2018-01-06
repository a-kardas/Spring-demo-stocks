package com.fp.stock.component.operations;

import com.fp.stock.dto.StockDTO;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

import java.security.Principal;

@Data
public class DeferredStackOperation extends DeferredResult<ResponseEntity<?>> {

    private Principal principal;

    private StockDTO stockDTO;

    private OperationType type;


}
