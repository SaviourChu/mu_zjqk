function rewardStatisticsesReportForm(date,json){
	//员工打赏总统计（天）
	if($("#ds-ztj").length > 0) {
		var myChart = echarts.init(document.getElementById('ds-ztj'));
		option = {
			color: ['#44b549', '#31acff', '#ff9933'],
			tooltip: {
				trigger: 'axis'
			},
			grid: {
				left: '3%',
				right: '10%',
				bottom: '8%',
				containLabel: true,
				width:1300
			},
			xAxis: [{
				type: 'category',
				boundaryGap: true,
				data: date
			}

			],
			yAxis: [{
				type: 'value'
			}],
			series: [{
				name: '注册用户人数',
				symbolSize: 6,
				type: 'line',
				itemStyle: {
					normal: {
						borderWidth: 1.5,
						lineStyle: {
							width: 2,
						},

					},
				},
				areaStyle: {
					normal: {
						color: '#daf0db',
					}
				},
				data: json
			},

			]
		};
		myChart.setOption(option);
	}

}



function hospitalReportFormr(date,json){
	//员工打赏总统计（天）
	if($("#ds-ztj").length > 0) {
		var myChart = echarts.init(document.getElementById('ds-ztj'));
		option = {
			color: ['#44b549', '#31acff', '#ff9933'],
			tooltip: {
				trigger: 'axis'
			},
			grid: {
				left: '3%',
				right: '10%',
				bottom: '8%',
				containLabel: true,
				width:1300
			},
			xAxis: [{
				type: 'category',
				boundaryGap: true,
				data: date
			}

			],
			yAxis: [{
				type: 'value'
			}],
			series: [{
				name: '认证医院数',
				symbolSize: 6,
				type: 'line',
				itemStyle: {
					normal: {
						borderWidth: 1.5,
						lineStyle: {
							width: 2,
						},

					},
				},
				areaStyle: {
					normal: {
						color: '#daf0db',
					}
				},
				data: json
			},

			]
		};
		myChart.setOption(option);
	}

};










$(function() {
	$(".ds-header li").click(function() {
		$(".ds-header li").eq($(this).index()).addClass("active").siblings().removeClass('active');
		$(".ds").hide().eq($(this).index()).show();
	});

	//日历
	var nowTemp = new Date();
	var nowDay = new Date(nowTemp.getFullYear(), nowTemp.getMonth(), nowTemp.getDate(), 0, 0, 0, 0).valueOf();
	var nowMoth = new Date(nowTemp.getFullYear(), nowTemp.getMonth(), 1, 0, 0, 0, 0).valueOf();
	var nowYear = new Date(nowTemp.getFullYear(), 0, 1, 0, 0, 0, 0).valueOf();
	var $myStart2 = $('#my-start-2');

	var checkin = $myStart2.datepicker({
		onRender: function(date, viewMode) {
			// 默认 days 视图，与当前日期比较
			var viewDate = nowDay;

			switch (viewMode) {
				// moths 视图，与当前月份比较
				case 1:
					viewDate = nowMoth;
					break;
				// years 视图，与当前年份比较
				case 2:
					viewDate = nowYear;
					break;
			}

			return date.valueOf() < viewDate ? 'am-disabled' : '';
		}
	}).on('changeDate.datepicker.amui', function(ev) {
		if (ev.date.valueOf() > checkout.date.valueOf()) {
			var newDate = new Date(ev.date)
			newDate.setDate(newDate.getDate() + 1);
			checkout.setValue(newDate);
		}
		checkin.close();
		$('#my-end-2')[0].focus();
	}).data('amui.datepicker');

	var checkout = $('#my-end-2').datepicker({
		onRender: function(date, viewMode) {
			var inTime = checkin.date;
			var inDay = inTime.valueOf();
			var inMoth = new Date(inTime.getFullYear(), inTime.getMonth(), 1, 0, 0, 0, 0).valueOf();
			var inYear = new Date(inTime.getFullYear(), 0, 1, 0, 0, 0, 0).valueOf();

			// 默认 days 视图，与当前日期比较
			var viewDate = inDay;

			switch (viewMode) {
				// moths 视图，与当前月份比较
				case 1:
					viewDate = inMoth;
					break;
				// years 视图，与当前年份比较
				case 2:
					viewDate = inYear;
					break;
			}

			return date.valueOf() <= viewDate ? 'am-disabled' : '';
		}
	}).on('changeDate.datepicker.amui', function(ev) {
		checkout.close();
	}).data('amui.datepicker');

})
