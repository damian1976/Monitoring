package org.indigo.openstackprobe.openstack;

import com.alibaba.fastjson.JSONException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * @author Reply Santer. This class is defined for getting the credentials from oszone.yml file in
 *         case IAM is skipped.
 */
public class OpenStackConfiguration {

  /**
   * The Constant log.
   **/
  private static final Logger log = LogManager.getLogger(OpenStackConfiguration.class);
  public static final String OS_ZONE_PROPERTY_FILE = "oszones.yml";
  public static String zone;

  /**
   * The openstack zones.
   **/
  private OpenStackZones openstackZones;

  /**
   * Instantiates a new openstack configuration.
   */
  public OpenStackConfiguration() {
    try {
      log.info("Retrieving openstack properties per zone");
      openstackZones = readYaml(getConfigFile(OS_ZONE_PROPERTY_FILE));

    } catch (Exception ex) {
      throw new RuntimeException("Failed to read property file " + OS_ZONE_PROPERTY_FILE, ex);
    }
  }

  /**
   * Instantiates a new openstack configuration used for test only.
   */
  public OpenStackConfiguration(String testZoneFile) {
    try {
      log.info("Retrieving openstack properties per zone");
      openstackZones = readYaml(getConfigFile(testZoneFile));

    } catch (Exception ex) {
      throw new RuntimeException("Failed to read property file " + OS_ZONE_PROPERTY_FILE, ex);
    }
  }

  /**
   * Read yaml.
   * 
   * @param file the file
   * @return the openstack zones
   */
  public OpenStackZones readYaml(final File file) {
    final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    OpenStackZones zoneFile = new OpenStackZones();
    try {
      zoneFile = mapper.readValue(file, OpenStackZones.class);
    } catch (JSONException | JsonMappingException je) {
      log.debug("Unable to parse the file {}", file, je);
    } catch (IOException ioe) {
      log.debug("Unable to get the file {}", file, ioe);
    }
    return zoneFile;
  }

  /**
   * Gets the config file.
   * 
   * @param file the filesS
   * @return the config file
   * @throws URISyntaxException uri
   */
  protected File getConfigFile(String file) throws URISyntaxException {
    String location = "";
    String opSystem = System.getProperty("os.name").toLowerCase();
    if (file.contains("test")) {
      URL url = getClass().getResource("/testoszones.yml");
      location = url.toURI().getPath();
    } else {
      if (opSystem.indexOf("win") >= 0) {
        location = "C://zabbixconfig//";
      } else {
        location = "/etc/zabbix/";
      }
      return new File(location + "/" + file);
    }
    return new File(location);
  }

  /**
   * Gets the openstack zones.
   * 
   * @return the openstack zones
   */
  public OpenStackZones getMonitoringZones() {
    return openstackZones;
  }
}
