package org.jeecg.modules.message.handle.impl;

import com.google.common.collect.Lists;

import java.util.*;

public class ListHelper {
	
	/**
	 * 去除重复的项， 返回被去除的
	 * @param <T>
	 * @param list
	 * @return
	 */
	@SuppressWarnings("hiding")
	public static <T extends Object> List<T> removeDuplicateWithOrder(List<T> list) {
		Set<T> set = new HashSet<T>();
		List<T> newList = new ArrayList<T>();
		List<T> removedList = new ArrayList<T>();
		for (Iterator<T> iter = list.iterator(); iter.hasNext();) {
			T element = iter.next();
			if (set.add(element)){
				newList.add(element);
			}else{
				removedList.add(element);
			}
		}
		list.clear();
		list.addAll(newList);
		return removedList;
	}
	
	
	/**
	 * 返回去重后的list，原list不变
	 * @param list
	 * @return
	 */
	public static List<String> distinctList(List<String> list){
		/*Set<String> set = Sets.newHashSet();
		for(String str : list){
			if(StringUtils.isNotBlank(str)){
				set.add(str);
			}
		}
		Iterator<String> it = set.iterator();
		list.clear();
		while(it.hasNext()){
			list.add(it.next());
		}*/
		List<String> newList = Lists.newArrayList();
		for(String str : list) {
			if(!newList.contains(str)) {
				newList.add(str);
			}
		}
		return newList;
	}
	
	@SuppressWarnings("hiding")
	public static <T extends Object> List<List<T>> splitList(List<T> list, int subCount){
		 int count = list.size() / subCount;
		 int yu = list.size() % subCount;
		 List<List<T>> result = Lists.newArrayList();
		 for (int i = 0; i < count; i++) {
			 List<T> subList = list.subList(i * subCount, (i + 1) * subCount);
			 result.add(subList);
		 }
		 
		 if(yu > 0){
			 List<T> subList = list.subList(count * subCount, count * subCount + yu);
			 result.add(subList);
		 }
		 
		 return result;
	}
	
	@SuppressWarnings("hiding")
	public static <T extends Object> List<T> subList(List<T> list, int fromIndex, int toIndex){
		int size = list.size();
		if(fromIndex >= size){ return Lists.newArrayList(); }
		toIndex = toIndex >= size ? size : toIndex;
		return list.subList(fromIndex, toIndex);
	}
	
	
	public static List<String> getDifferentItems(List<String> list1, List<String> list2){
		List<String> differentItems = Lists.newArrayList();
		
		for(String str1 : list1){
            if(!list2.contains(str1)){
            	differentItems.add(str1);
            }
        }

        for(String str2 : list2){
            if(!list1.contains(str2)){
            	differentItems.add(str2);
            }
        }
        
        return differentItems;
    }
	
	public static void main(String[] args){
		List<String> list1 = Lists.newArrayList("1","2","1");
		System.out.println(removeDuplicateWithOrder(list1));
		System.out.println(list1);
		
//		List<Integer> tempList = Lists.newArrayList(1,2,3,4,5);
//		System.out.println(subList(tempList, 0, 5));
	}
}
