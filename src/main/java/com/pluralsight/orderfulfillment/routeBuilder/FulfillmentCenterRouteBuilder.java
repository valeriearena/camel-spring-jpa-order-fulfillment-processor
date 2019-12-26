package com.pluralsight.orderfulfillment.routeBuilder;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.xml.Namespaces;
import org.springframework.stereotype.Component;

/**
 * Routes the message from the ORDER_ITEM_PROCESSING queue to the appropriate queue
 * based on the fulfillment center element of the message.
 *
 * As the message from the ORDER_ITEM_PROCESSING queue is XML, a namespace is required.
 *
 * A Choice processor is used to realize the Content-Based Router.
 * When the Fulfillment Center element is equal to the value of the ABC fulfillment enter enumeration,
 *    the message will be routed to the ABC fulfillment center request queue.
 * When the Fulfillment Center element is equal to the value of the Fulfillment Center 1 enumeration value,
 *    the message will be routed to the Fulfillment Center 1 request queue.
 * If a message comes in with a Fulfillment Center element value that is unsupported,
 *    the message gets routed to an error queue.
 *
 * An XPath expression is used to lookup the fulfillment center value using the specified namespace.
 *
 * Below is a snippet of the XML returned by the ORDER_ITEM_PROCESSING queue.
 *
 * <Order xmlns="http://www.pluralsight.com/orderfulfillment/Order">
 * <OrderType> <FulfillmentCenter>ABCFulfillmentCenter</FulfillmentCenter>
 *
 */
@Component
public class FulfillmentCenterRouteBuilder extends RouteBuilder {

  @Override
  public void configure() throws Exception {

    getContext().setTracing(true);

    Namespaces namespace = new Namespaces("o","http://www.pluralsight.com/orderfulfillment/Order");

    // Send from the ORDER_ITEM_PROCESSING queue to the correct fulfillment center queue.
    from("activemq:queue:ORDER_ITEM_PROCESSING")
        .choice()
        .when()
        .xpath(
            "/o:Order/o:OrderType/o:FulfillmentCenter = '"
                + com.pluralsight.orderfulfillment.generated.FulfillmentCenter.ABC_FULFILLMENT_CENTER.value()
                + "'", namespace)
        .to("activemq:queue:ABC_FULFILLMENT_REQUEST")
        .when()
        .xpath(
            "/o:Order/o:OrderType/o:FulfillmentCenter = '"
                + com.pluralsight.orderfulfillment.generated.FulfillmentCenter.FULFILLMENT_CENTER_ONE.value()
                + "'", namespace)
        .to("activemq:queue:FC1_FULFILLMENT_REQUEST").otherwise()
        .to("activemq:queue:ERROR_FULFILLMENT_REQUEST");
  }
}