/**
 * 
 */
package com.bu6ido.bitpower.common;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

import com.maxmind.geoip.LookupService;

/**
 * @author bu6ido
 *
 */
public class GeoLocationService 
{
	private static GeoLocationService service = null;
	
	private LookupService lookupService;
	
	private GeoLocationService()
	{
		String sep = File.separator;
		
		String dbfile = "." + sep + "res" + sep + "GeoIP.dat";
		try
		{
			lookupService = new LookupService(dbfile, LookupService.GEOIP_MEMORY_CACHE);
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
	}
	
	public static synchronized GeoLocationService getInstance()
	{
		if (service == null)
		{
			service = new GeoLocationService();
		}
		return service;
	}
	
	public String getCountry(InetAddress addr)
	{
		if (lookupService == null) return null;
		if (addr == null) return null;
		return lookupService.getCountry(addr).getName();
	}
}
