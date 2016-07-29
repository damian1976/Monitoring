/**
Copyright 2016 ATOS SPAIN S.A.

Licensed under the Apache License, Version 2.0 (the License);
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

Authors Contact:
Francisco Javier Nieto. Atos Research and Innovation, Atos SPAIN SA
@email francisco.nieto@atos.net
**/

package org.indigo.occiprobe.openstack;

import java.io.IOException;
import java.io.InputStream;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

/**
 * This class is focused on retrieving configuration parameters, serving them to other classes
 * which may need them in an easy way. It loads the configuration set in the occiprobe.properties
 * file.
 * @author ATOS
 *
 */
public class PropertiesManager {
  public static final String KEYSTONE_LOCATION = "openstack.keystoneurl";
  public static final String KEYSTONE_PORT = "openstack.keystoneport";
  public static final String OCCI_LOCATION = "openstack.occiurl";
  public static final String OCCI_PORT = "openstack.occiport";
  public static final String OPENSTACK_USER = "openstack.user";
  public static final String OPENSTACK_PASSWORD = "openstack.password";
  public static final String JAVA_KEYSTORE = "java.keystore";
  public static final String ZABBIX_IP = "zabbix.ip";
  public static final String ZABBIX_SENDER = "zabbix.sender.location";
  public static final String CMDB_URL = "cmdb.location";
  
  private HashMap<String, String> propertiesList;
  
  /**
   * This is the constructor of the class. It loads the properties file and it
   * retrieves all the properties available in the file, setting the values of the
   * configuration variables.
   */
  public PropertiesManager() {
    propertiesList = new HashMap<String, String>();
    readProperties();
  }  
  
  private void readProperties() {
    Properties prop = new Properties();
         
    // Get class loader, for avoiding problems with Tomcat (loading local files)
    ClassLoader loader = PropertiesManager.class.getClassLoader();
    if (loader == null) {
      loader = ClassLoader.getSystemClassLoader();
    }
    
    try {
      // We want to load file located in WEB-INF/classes/
      String fileName = "occiprobe.properties";
      InputStream is = loader.getResourceAsStream(fileName);
      prop.load(is);
    } catch (IOException e) {
      System.out.println(e.toString());
    }
    
    //Put all the properties into the map
    for (Enumeration e = prop.keys(); e.hasMoreElements() ; ) {
      // Get the property
      String obj = (String) e.nextElement();
      propertiesList.put(obj, prop.getProperty(obj));           
    }
  }
  
  /**
   * It provides the current value of the configuration property requested.
   * @param propertyName An String with a valid property name.
   * @return A String with the value loaded from the configuration file.
   */
  public String getProperty(String propertyName) {
    return propertiesList.get(propertyName);
  }
  
  /**
   * Typical main method for testing.
   * @param args Typical args.
   */
  public static void main(String[] args) {
    PropertiesManager myProp = new PropertiesManager();
    System.out.println("KEYSTONE URL: " + myProp.getProperty("openstack.keystoneurl"));
  }
}