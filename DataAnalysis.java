/*******************************************************************************
For: Steve Montoya at Conversant Media
From: Bob Schaefer 1644 Amador Ln. Newbury Park Ca. 91320
Position: https://alliancedata.taleo.net/careersection/jobdetail.ftl?job=0078061&lang=en
*******************************************************************************/

import java.util.*;
import java.io.*;
import java.text.*;
import java.sql.*;
import java.time.*;
import java.math.*;

public class DataAnalysis
{

	//ArrayLists containing data from file. 
	private static ArrayList<String> transactionData  =  new ArrayList<String>();
	private static ArrayList<String> dataCenterITime  =  new ArrayList<String>(); 
	private static ArrayList<String> dataCenterIValue =  new ArrayList<String>();
	private static ArrayList<String> dataCenterSTime  =  new ArrayList<String>();
	private static ArrayList<String> dataCenterSValue =  new ArrayList<String>();
	private static ArrayList<String> dataCenterATime  =  new ArrayList<String>();
	private static ArrayList<String> dataCenterAValue =  new ArrayList<String>();
	
	private static String requestType="rtb.Requests";
	private static String datacenterI="dc=I";
	private static String datacenterA="dc=A";
	private static String datacenterS="dc=S";
	private static String value="Value";
	private static String data_center = "Data center";
	
	
	public static void main(String[] args)
	{
		System.out.println("       Data Analysis starting       ");
		
		BufferedReader csvBuffer = null;
		
		try
		{
			//Read data in from text file. 
			String rawDataLine;
			System.out.println("=======================================================");
			System.out.println("Raw data being read from file");
			//csvBuffer = new BufferedReader(new FileReader("/temp/Coding_exercise/data.Montoya.csv"));
			csvBuffer = new BufferedReader(new FileReader("data.Montoya.csv"));
			System.out.println("Raw data read complete");
						
			ArrayList<String> tempResult = new ArrayList<String>();
			
			// Read csv file line by line.
			while ((rawDataLine = csvBuffer.readLine()) != null)
			{
				//debug to view raw data lines being read in from buffer. 
				//System.out.println("Raw CSV data: " + rawDataLine);
				if (rawDataLine != null)
			    {
					String[] splitData = rawDataLine.split("\\s*,\\s*");
		
		            for (int i = 0; i < splitData.length; i++)
		            {
			          if (!(splitData[i] == null) || !(splitData[i].length() == 0))
			             tempResult.add(splitData[i].trim());
				    }
			    }
			}
			
            //Copy temp array to permanent array structure to hold the loaded data. 
            //Use temp array later to split out data into additional array structures.			
			transactionData = tempResult;
				
		}
		catch (IOException e)
		{
		  e.printStackTrace();
		}
		finally
		{
			try
			{
				if (csvBuffer != null)
				csvBuffer.close();
			}
			catch (IOException IOE)
			{
				IOE.printStackTrace();
			}
		}
		
		// debug code to view the contents of the initial loading of the csv file to the array. 
		//dumpTransactionData();
		
		//Split initial array data into separate arrays representing the 3 different data centers. 
		splitOutTransactionData();
		
		//Total number of transactions per data center
		//Total number of transactions across data centers
		getTransactionCount();
		
		//Get transaction amount over time period per data center. 
		getTranCountOverTime();
		
		//get Average value for each data center and total average for all data centers. 
		getAverageValue();
	}
	public static void dumpTransactionData()
	{
		// Debug code to dump out individual datacenter time and value arrays. 
		ListIterator lIterator = null;
		
		lIterator = dataCenterITime.listIterator();
		while(lIterator.hasNext())
		System.out.println("***** lIteratorITime ArrayList Element: " + lIterator.next());
		lIterator = dataCenterIValue.listIterator();
		while(lIterator.hasNext())
	    System.out.println("***** lIteratorIValue  ArrayList Element: " + lIterator.next());
	    lIterator = dataCenterSTime.listIterator();
		while(lIterator.hasNext())
		System.out.println("***** lIteratorSTime ArrayList Element: " + lIterator.next());
		lIterator = dataCenterSValue.listIterator();
		while(lIterator.hasNext())
	    System.out.println("***** lIteratorSValue  ArrayList Element: " + lIterator.next());
	     lIterator = dataCenterATime.listIterator();
		while(lIterator.hasNext())
		System.out.println("***** lIteratorATime ArrayList Element: " + lIterator.next());
		lIterator = dataCenterAValue.listIterator();
		while(lIterator.hasNext())
	    System.out.println("***** lIteratorAValue  ArrayList Element: " + lIterator.next());	
	    
	}
	public static void splitOutTransactionData()
	{
		
		Long endMillisec = null;
		Long startMillisec = System.currentTimeMillis();
		Long totalMillisec = null;
		
		//Split the data from the text file into separate arrays representing the 3 data centers. 
		System.out.println("=======================================================");
		System.out.println("Splitting out transactions by data center: Started" ) ;
		
		ArrayList<String> tempTransactionData = new ArrayList<String>();
		tempTransactionData = transactionData;
		List tempList = null;
		String datacenterTemp = null;
		
						
		while(tempTransactionData.size() > 0)
	    {
		   datacenterTemp = (String)tempTransactionData.get(3);
		   
		   if(datacenterTemp.equals(datacenterI))
		   {
			   dataCenterITime.add(tempTransactionData.get(1));
			   dataCenterIValue.add(tempTransactionData.get(2));
			   tempTransactionData.remove(3);
			   tempTransactionData.remove(2);
			   tempTransactionData.remove(1);
			   tempTransactionData.remove(0);
		   }
		   if(datacenterTemp.equals(datacenterA))
		   {
			   dataCenterATime.add(tempTransactionData.get(1));
	           dataCenterAValue.add(tempTransactionData.get(2));
			   tempTransactionData.remove(3);
			   tempTransactionData.remove(2);
			   tempTransactionData.remove(1);
			   tempTransactionData.remove(0);
		   }
		   if(datacenterTemp.equals(datacenterS))
		   {
			   dataCenterSTime.add(tempTransactionData.get(1));
	           dataCenterSValue.add(tempTransactionData.get(2));
			   tempTransactionData.remove(3);
			   tempTransactionData.remove(2);
			   tempTransactionData.remove(1);
			   tempTransactionData.remove(0);
		   }
		   if(datacenterTemp.equals(data_center))
		   {
			   tempTransactionData.remove(3);
			   tempTransactionData.remove(2);
			   tempTransactionData.remove(1);
			   tempTransactionData.remove(0);
		   }
		   
		  
		}
		
		endMillisec = System.currentTimeMillis();
		totalMillisec = endMillisec - startMillisec;
			
        System.out.println("Splitting out transactions by data center: Ended ");
		System.out.println("Total time: "  + totalMillisec + " milliseconds");
		System.out.println("=======================================================");
		
			
		
	}
	public static void getTransactionCount()
	{
		//Count number of total transactions. 
		System.out.println("Transaction count by data center:  ");
		System.out.println("Total number of " + requestType +  " transactions for data center " + "A" + " " + "= " + dataCenterITime.size());
		
		System.out.println("Total number of " + requestType +  " transactions for data center " + "I" + " " + "= " + dataCenterATime.size());
		
		System.out.println("Total number of " + requestType +  " transactions for data center " + "S" + " " + "= " + dataCenterSTime.size());
		
		int totalTrans = dataCenterATime.size() + dataCenterSTime.size() + dataCenterITime.size();
		
		System.out.println("-------------------------------------------------------");
		System.out.println("Number of total transactions across data centers = "  + totalTrans);
		System.out.println("=======================================================");
	
	}
	
	public static void getTranCountOverTime()
	{
		
		//Calculate transaction count over time for each data center. 
		//Calculate transaction count over time for all three data centers combined. 
		System.out.println("Transaction count over time:  ");
				
		Time startTime = null;
		Time endTime = null;
		Duration durationTime = null;
		String startTimeStr = null;
		String endTimeStr = null;
		Long longStartTime = null;
		Long longEndTime = null;
		Instant startInstant = null;
		Instant endInstant = null;
		int size;
		
		//Get transactions over time for datacenter A.		
		size = dataCenterATime.size();
		startTimeStr = dataCenterATime.get(0);
		endTimeStr = dataCenterATime.get(size - 1);
    	longStartTime = new Long(startTimeStr);
		longEndTime = new Long(endTimeStr);
		startTime = new Time(longStartTime);
		endTime   = new Time(longEndTime);
		
		startInstant = Instant.ofEpochMilli(longStartTime.longValue());
		endInstant   = Instant.ofEpochMilli(longEndTime.longValue());
		durationTime = Duration.between(startInstant, endInstant );
		
		System.out.println("Datacenter A " + requestType + " Start time: " + startTime);	 
        System.out.println("Datacenter A " + requestType + " End time: " + endTime);		
		System.out.println("Datacenter A processed " + size + " " + requestType + " transactions in " + durationTime.toMinutes() + " minutes");
		
		//Get transactions over time for datacenter I.	
		size = dataCenterITime.size();
		startTimeStr = dataCenterITime.get(0);
		endTimeStr = dataCenterITime.get(size - 1);
    	longStartTime = new Long(startTimeStr);
	    longEndTime = new Long(endTimeStr);
		startTime = new Time(longStartTime);
		endTime   = new Time(longEndTime);
		
		startInstant = Instant.ofEpochMilli(longStartTime.longValue());
		endInstant   = Instant.ofEpochMilli(longEndTime.longValue());
		durationTime = Duration.between(startInstant, endInstant );
			 
        System.out.println("Datacenter I " + requestType + " Start time: " + startTime);	 
        System.out.println("Datacenter I " + requestType + " End time: " + endTime);					 
		System.out.println("Datacenter I processed " + size + " " + requestType + " transactions in " + durationTime.toMinutes() + " minutes");
		
        //Get transactions over time for datacenter S.			
		size = dataCenterSTime.size();
		startTimeStr = dataCenterSTime.get(0);
		endTimeStr = dataCenterSTime.get(size - 1);
    	longStartTime = new Long(startTimeStr);
		longEndTime = new Long(endTimeStr);
		startTime = new Time(longStartTime);
		endTime   = new Time(longEndTime);
		
		startInstant = Instant.ofEpochMilli(longStartTime.longValue());
		endInstant   = Instant.ofEpochMilli(longEndTime.longValue());
		durationTime = Duration.between(startInstant, endInstant );
		
		System.out.println("Datacenter S " + requestType + " Start time: " + startTime);	 
        System.out.println("Datacenter S " + requestType + " End time: " + endTime);			  
		System.out.println("Datacenter S processed " + size + " " + requestType + " transactions in " + durationTime.toMinutes() + " minutes");
		System.out.println("=======================================================");
		
	}
	public static void getAverageValue()
	{
		// Get average transaction value for each data center. 
		// Average with and without the negative values 
		ListIterator lIterator = null;
		BigDecimal sum = new BigDecimal(0);
		BigDecimal average = new BigDecimal(0);
		BigDecimal size = null;
		
		//Calculate the averag value for data center A. 
		lIterator = dataCenterAValue.listIterator();
		size = new BigDecimal(dataCenterAValue.size());
		sum = getSumValue(lIterator);
		
        System.out.println("Transaction sum by data center (Includes negative values):  ");
		System.out.println("Datacenter A " + requestType + " transaction total sum value= " + sum);
		sum = sum.divide(size,6);
		System.out.println("Datacenter A average " + requestType + " transaction value= " + sum);
		
		//Calculate the average value for data center I. 
		lIterator = dataCenterIValue.listIterator();
		size = new BigDecimal(dataCenterIValue.size());
		sum = getSumValue(lIterator);
				
		System.out.println("Datacenter I " + requestType + " transaction total sum value= " + sum);
		sum = sum.divide(size,6);
		System.out.println("Datacenter I average " + requestType + " transaction value= " + sum);
		
		//Calculate the average value for data center S. 
		lIterator = dataCenterSValue.listIterator();
		size = new BigDecimal(dataCenterSValue.size());
		sum = getSumValue(lIterator);
				
		System.out.println("Datacenter S " + requestType + " transaction total sum value= " + sum);
		sum = sum.divide(size,6);
		System.out.println("Datacenter S average " + requestType + " transaction value= " + sum);
				
		//Calculate average for all data centers
        lIterator = dataCenterAValue.listIterator();
		size = new BigDecimal(dataCenterAValue.size());
		sum = getSumValue(lIterator);
				
        lIterator = dataCenterIValue.listIterator();
		size = size.add(new BigDecimal(dataCenterIValue.size()));
		sum = sum.add(getSumValue(lIterator));
		        
		lIterator = dataCenterSValue.listIterator();
		size = size.add(new BigDecimal(dataCenterSValue.size()));
		sum = sum.add(getSumValue(lIterator));
		        
		System.out.println("-------------------------------------------------------");
        System.out.println("All datacenters " + requestType + " transaction total value sum = " + sum);
		sum = sum.divide(size,6);
		System.out.println("All datacenters " + requestType + " transaction total value= " + sum);		
	}
	
	public static BigDecimal getSumValue(ListIterator lIterator)
	{
		
		//Return BigDecimal object containing the sum of array elements. 
		BigDecimal sum = new BigDecimal(0);
		
		while(lIterator.hasNext())
		{
			try
			{
				sum = sum.add(new BigDecimal((String)lIterator.next()));
			}
			catch(Exception ex)
			{
			   System.out.println("Transaction value sum calculation exception = " + ex);
			}
		}
		
	  return sum;	
    }
	
	 
	
};

