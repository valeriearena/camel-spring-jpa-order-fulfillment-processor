package com.pluralsight.orderfulfillment.routeBuilder;

import com.pluralsight.orderfulfillment.generated.FulfillmentCenter;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.xml.Namespaces;

/**
 * Camel RouteBuilder to implement a Content-Based Router.
 *
 * ActiveMQ component URI syntax: activemq:[queue:|topic:]destinationName[?options]
 *
 * Routes the message from the ORDER_ITEM_PROCESSING queue to the appropriate queue based on the <FulfillmentCenter> element of the message.
 *
 * A choice processor is used to implement a Content-Based Router (which is a simple message router).
 * When the <FulfillmentCenter> element is equal to 'ABCFulfillmentCenter', the message will be routed to the ABC_FULFILLMENT_REQUEST queue.
 * When the <FulfillmentCenter> element is equal to 'FulfillmentCenterOne', the message will be routed to the FC1_FULFILLMENT_REQUEST queue.
 * If a message comes in with an unsupported value, the message gets routed to ERROR_FULFILLMENT_REQUEST.
 *
 * An XPath expression is used to lookup the <FulfillmentCenter> value.
 * - The xpath expression evaluates the inbound message of the exchange. The body of the message is xml.
 * - The when expression uses xpath expression to evaluates the type of fulfillment center by retrieving node from xml message body.
 * - The xpath expression will return true or false depending on the value of the 'FulfillmentCenter' xml node.
 * - The value of the expression (conditional) will determine where the message gets routed.
 *
 * NOTE: You can choose from many expression languages in Camel. For ex.: SpEL, JXPath, MVEL, OGNL, JavaScript, Groovy, XPath, and XQuery.
 *
 * Below is a snippet of the XML returned by the ORDER_ITEM_PROCESSING queue.
 *
 * <Order xmlns="http://www.pluralsight.com/orderfulfillment/Order">
 * 	<OrderType>
 *         <FirstName>Ron</FirstName>
 *         <LastName>River</LastName>
 *         <Email>ron@goodbye.com</Email>
 *         <OrderNumber>1005</OrderNumber>
 *         <TimeOrderPlaced>2019-12-17T06:39:50.993-05:00</TimeOrderPlaced>
 *         <FulfillmentCenter>ABCFulfillmentCenter</FulfillmentCenter>
 *         <OrderItems>
 *             <ItemNumber>078-1344200444</ItemNumber>
 *             <Price>20.00000</Price>
 *             <Quantity>3</Quantity>
 *         </OrderItems>
 *     </OrderType>
 * </Order>
 */
//@Component
public class FulfillmentCenterRouteBuilder extends RouteBuilder {

  @Override
  public void configure() throws Exception {

    //getContext().setTracing(true);

    // Java DSL is easy to read and understand!!!

    // We need the namespace so that we can look up the XML element correctly in XPATH.
    Namespaces namespace = new Namespaces("o", "http://www.pluralsight.com/orderfulfillment/Order");

    from("activemq:queue:ORDER_ITEM_PROCESSING")
        .choice()
          .when()
            .xpath("/o:Order/o:OrderType/o:FulfillmentCenter = '" + FulfillmentCenter.ABC_FULFILLMENT_CENTER.value() + "'", namespace)
            .to("activemq:queue:ABC_FULFILLMENT_REQUEST")
          .when()
            .xpath("/o:Order/o:OrderType/o:FulfillmentCenter = '" + FulfillmentCenter.FULFILLMENT_CENTER_ONE.value() + "'", namespace)
            .to("activemq:queue:FC1_FULFILLMENT_REQUEST")
          .otherwise()
            .to("activemq:queue:ERROR_FULFILLMENT_REQUEST");
  }
}
