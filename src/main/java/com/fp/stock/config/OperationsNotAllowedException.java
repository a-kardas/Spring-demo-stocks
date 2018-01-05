package com.fp.stock.config;


public class OperationsNotAllowedException extends Exception {

    public final static String NOT_ENOUGH_MONEY = "Operation not allowed - not enough money.";
    public final static String NOT_ENOUGH_UNITS_ON_STOCK = "Operation not allowed - there's no enough unit on the stock exchange";
    public final static String DEFAULT = "Operation not allowed";
    public final static String AMOUNT_IS_NOT_MULTIPLE = "Operation not allowed - you can buy/sell only multiple of the basic unit";
    public final static String USER_DONT_HAVE_ENOUGH_UNITS = "Operation not allowed - you don't have enough units";

    public OperationsNotAllowedException(String message){
        super(message);
    }

}
