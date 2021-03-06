package com.pluralsight.orderfulfillment.config;

import com.pluralsight.orderfulfillment.order.DefaultOrderService;
import com.pluralsight.orderfulfillment.order.OrderController;
import com.pluralsight.orderfulfillment.order.OrderService;
import com.pluralsight.orderfulfillment.order.fulfillment.FulfillmentCommand;
import com.pluralsight.orderfulfillment.order.fulfillment.FulfillmentProcessor;
import com.pluralsight.orderfulfillment.order.fulfillment.NewOrderRetrievalCommand;
import com.pluralsight.orderfulfillment.order.fulfillment.NewOrderSendToFulfillmentCenterOneCommand;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Main application configuration for the order fulfillment processor.
 *
 * Both Spring can be configured via Java annotations or XML.
 * Java configuration is recommended:
 * 1. Java configurations gives type safety and can be checked at compile time. XML configuration is only checked at runtime.
 * 2. Easier to work with in IDE - code completion, refactoring, finding references, etc.
 * 3. Complex configurations in XML can be hard to read and maintain.
 *
 * NOTE: When Spring sees @Bean, it will execute the method and register the return value as a bean within Spring context.
 * By default, the bean name will be the same as the method name.
 *
 * @author Michael Hoffman, Pluralsight
 */
@Configuration
@ComponentScan("com.pluralsight.orderfulfillment.config") //tells Spring to scan the current package and all of its sub-packages
@PropertySource("classpath:order-fulfillment.properties")
public class AppConfig {

  // ************* Spring beans used by the code that was refactored and replaced by Camel routes. *************

  @Bean
  public OrderController orderController(){
    return new OrderController();
  }

  @Bean
  public OrderService orderService() {
    return new DefaultOrderService();
  }

  @Bean
  public FulfillmentProcessor fulfillmentProcessor() {
    return new FulfillmentProcessor();
  }

  @Bean
  public FulfillmentCommand newOrderRetrievalCommand() {
    return new NewOrderRetrievalCommand();
  }

  @Bean
  public FulfillmentCommand newOrderSendToFulfillmentCenterOneCommand() {
    return new NewOrderSendToFulfillmentCenterOneCommand();
  }

}
