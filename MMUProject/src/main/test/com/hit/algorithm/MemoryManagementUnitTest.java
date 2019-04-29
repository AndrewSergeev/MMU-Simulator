package com.hit.algorithm;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.hit.memoryunits.MemoryManagementUnit;
import com.hit.memoryunits.Page;

public class MemoryManagementUnitTest {
	
	@SuppressWarnings("unchecked")
	@Test
	public void testMMU() throws FileNotFoundException, IOException
	{		
		int capacityRam = 5;
		IAlgoCache<Long,Long> algo = new LRUAlgoCacheImpl<Long,Long>(capacityRam);
		MemoryManagementUnit mmu = new MemoryManagementUnit(capacityRam, algo);		
		
		/////from hd to ram
		Page<byte[]>[] requestedPages = mmu.getPages(new Long[]{1L, 2L, 3L});
		Page<byte[]>[] expectedPages = new Page[3];
		//System.out.println(mmu.getRam().getPages().toString());
		for(int i = 0; i < expectedPages.length; i++)
		{
			Long num = (long) i + 1;
			expectedPages[i] = new Page<byte[]>(num, num.toString().getBytes());
		}
		
		Assert.assertNull(mmu.getAlgo().getElement(0L));
		Assert.assertArrayEquals(expectedPages, requestedPages);///maybe need to assert each element
		
		//////from ram only
		Page<byte[]>[] requestedPagesFromRam = mmu.getPages(new Long[]{1L, 2L});
		Page<byte[]>[] expectedPagesFromRam = new Page[2];
		//System.out.println(mmu.getRam().getPages().toString());
		for(int i = 0; i < 2; i++)
		{
			Long num = (long) i + 1;
			expectedPagesFromRam[i] = new Page<byte[]>(num, num.toString().getBytes());
		}
		
		Assert.assertArrayEquals(expectedPagesFromRam, requestedPagesFromRam);
		
		///////from hd and from ram
		Page<byte[]>[] requestedPagesFromBoth = mmu.getPages(new Long[]{2L, 5L, 6L});
		Page<byte[]>[] expectedPagesFromBoth = new Page[3];
		Long[] nums = new Long[]{2L, 5L, 6L};
		int index = 0;
		//System.out.println(mmu.getRam().getPages().toString());
		for(Long num: nums)
		{
			if(num != null)
			{
				expectedPagesFromBoth[index] = new Page<byte[]>(num, num.toString().getBytes());
				index++;
			}			
		}		
		
		Assert.assertArrayEquals(expectedPagesFromBoth, requestedPagesFromBoth);
		
		//////checking ram size
		Assert.assertEquals(5, mmu.getRam().getPages().size());
		
		///////checking with full ram
		Page<byte[]>[] requestedPagesFullRam = mmu.getPages(new Long[]{1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L});
		Page<byte[]>[] expectedPagesFullRam  = new Page[10];
		//System.out.println("!!!!!!!!!" + mmu.getRam().getPages().toString());
		for(int i = 0; i < 10; i++)
		{
			Long num = (long) i + 1;
			expectedPagesFullRam[i] = new Page<byte[]>(num, num.toString().getBytes());
		}
		
		Page<byte[]>[] requestedPagesFullRamSorted = new Page[requestedPagesFullRam.length];
		Map<Long,Page<byte[]>> requestedPagesFullRamMap = new LinkedHashMap<Long,Page<byte[]>>();
		//sorting:
		for(Page<byte[]> page : requestedPagesFullRam)
		{
			requestedPagesFullRamMap.put(page.getPageId(), page);
		}
		
		for(int i = 0; i < expectedPagesFullRam.length; i++)
		{
			Long id = expectedPagesFullRam[i].getPageId();
			requestedPagesFullRamSorted[i] = requestedPagesFullRamMap.get(id);			
		}
		//end sorting
		Assert.assertArrayEquals(expectedPagesFullRam, requestedPagesFullRamSorted);
		
		
//		System.out.println("RAM JUNIT" + mmu.getRam().getPages() + "\n");
//		mmu.shutDown();
//		
//		
//		int capacityRam1 = 5;
//		IAlgoCache<Long,Long> algo1 = new LRUAlgoCacheImpl<Long,Long>(capacityRam1);
//		MemoryManagementUnit mmu1 = new MemoryManagementUnit(capacityRam1, algo1);	
//		
//		Page<byte[]>[] requestedPages1 = mmu1.getPages(new Long[]{1L, 2L, 3L});
//		System.out.println("HD IN JUNIT  " + HardDisk.getInstance().getPagesInHD().toString());	
//		Page<byte[]>[] expectedPages1 = new Page[3];
//		System.out.println(mmu1.getRam().getPages().toString());
//		for(int i = 0; i < expectedPages1.length; i++)
//		{
//			Long num = (long) i + 1;
//			expectedPages1[i] = new Page<byte[]>(num, num.toString().getBytes());
//		}
//		
//		Assert.assertNull(mmu1.getAlgo().getElement(0L));
//		Assert.assertArrayEquals(expectedPages1, requestedPages1);
		
	}
}
