package com.ofrick;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.OMText;


public class SampleOrderProcessingService {

 private HashMap<String, Double> orders = new HashMap<String, Double>(); 
 
 public static final String OMnamespace = "http://www.ofrick.com";
 
 OMFactory factory = OMAbstractFactory.getOMFactory();
    OMNamespace OMNamespaceObj = factory.createOMNamespace(OMnamespace, "ns");
 
    public void AddOrder(OMElement element) throws XMLStreamException{
  element.build();
        element.detach();
        
        OMElement symbol = element.getFirstChildWithName(new QName(OMnamespace, "symbol"));
        OMElement price = element.getFirstChildWithName(new QName(OMnamespace, "price"));
       
        orders.put(symbol.getText(), new Double(price.getText()));
        
        System.out.println("Order Added. [Symbol=" + symbol.getText() 
                           + ", Price=" + price.getText() +"]");
      }

     public OMElement GetPrice(OMElement element)throws XMLStreamException{
  element.build();
        element.detach();

        OMElement symbol = element.getFirstChildWithName(new QName(OMnamespace, "symbol"));
        
        String result = "NONE";
        if (orders.containsKey(symbol.getText())){
          Double price = (Double) orders.get(symbol.getText());
          result = Double.toString(price);
        }

        //Root element of the response can have any name
        OMElement root = factory.createOMElement("result", OMNamespaceObj);
        OMElement value = factory.createOMElement("value", OMNamespaceObj);
        OMText resultText = factory.createOMText(value, result);
        
        value.addChild(resultText);
        root.addChild(value);
        
        System.out.println("Price Retrived. [Symbol=" + symbol.getText() + ", Price=" + value.getText() +"]");
        
        return root;
     }

     public OMElement GetOrders(OMElement element)throws XMLStreamException{
  element.build();
        element.detach();
        
  Iterator<Entry<String, Double>> it = orders.entrySet().iterator();
  
  OMElement root = factory.createOMElement("OrderQueryResult", OMNamespaceObj);
  
     while (it.hasNext()) {
         Entry<String, Double> pair = it.next();
         
         OMElement order = factory.createOMElement("Order", OMNamespaceObj);
         OMElement symbol = factory.createOMElement("symbol", OMNamespaceObj);
         OMElement price = factory.createOMElement("price", OMNamespaceObj);
         
         OMText symbolText = factory.createOMText(symbol, pair.getKey());
         OMText priceText = factory.createOMText(price, Double.toString(pair.getValue()));
         
         symbol.addChild(symbolText);
         price.addChild(priceText);
         order.addChild(symbol);
         order.addChild(price);
         root.addChild(order);
         
         System.out.println("Oder Retrived. [Symbol=" + pair.getKey() + ", Price=" + pair.getValue().toString() +"]");
     }
  return root;
 }
}