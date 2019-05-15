package com.common.kits;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

public class PageUtils<T> {

	public static <T> Map<String, Object> page(Page<T> results) {
		Map<String, Object> result = Maps.newHashMap();
		result.put("total", results.getTotalRow());
		result.put("rows", results.getList());
		return result;
	}

	public static Map<String, Object> pageFooter(Page<Record> results, String... fields) {
		Map<String, Object> result = Maps.newHashMap();
		result.put("total", results.getTotalRow());
		List<Record> rows = results.getList();
		result.put("rows", rows);
		List<String> fieldList = Lists.newArrayList(fields);
		List<Object> footer = Lists.newArrayList();
		fieldList.forEach(field -> {
			Map<String, Double> data = Maps.newHashMap();
			Double sum = rows.stream().mapToDouble(t -> t.get(field)).sum();
			data.put(field, sum);
			footer.add(data);
		});
		result.put("footer", footer);
		return result;
	}

	public static void main(String[] args) {
		List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
		Stream<Integer> stream = numbers.stream();
		stream.filter((x) -> {
			return x % 2 == 0;
		}).map((x) -> {
			return x * x;
		}).forEach(System.out::println);
	}
}
