/*

Problem:
  1. In case, we want to add an adapter in between to achieve a goal which has a different input mechanism than required.
  2. Client needs to call an external third party method but they do not know how to call them then we can use an adapter
      in between which converts the intended call into required format which third party wants.
  3. ex: 
    Get the jsonData whereas the inital raw data is present in XmlFormat

Architecture:
  1. An external class with a method with unknown type (like XmlData class)
  2. Internal/Client class with known format to be called (like jsonData needed)
  3. An interface for the adapter whose methods the client/internal class can call
  4. Concrete Adapter implementing the interface with has-a external class object which can convert the required details
    by calling same internal method. Inside the internal method, call the external method and convert into required format.

Implementation: */


class Client {
    IAdapter adapter;
    
    // call adapter.getJsonData(); inside this class anywhere.
}

interface IAdapter {
    public String getJsonData();
}

class JsonToXmlDataAdapter implements {
    XmlDataProvider xmlDataProvider;
    
    public String getJsonData() {
        String data = xmlDataProvider.getXmlData();
        System.out.println("converted to jsondata");
        return data;
    }
} 

class XmlDataProvider {
    public String getXmlData() {
        return "xmlData";
    }
}
