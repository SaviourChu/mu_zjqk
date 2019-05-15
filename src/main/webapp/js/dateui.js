$.extend($.fn.combobox.methods, {
	yearandmonth : function(jq) {
		return jq.each(function() {
			var obj = $(this).combobox();
			var date = new Date();
			var year = date.getFullYear();
			var month = date.getMonth() + 1;
			var table = $('<table>');
			var tr1 = $('<tr>');
			var tr1td1 = $('<td>', {
				text : '-',
				click : function() {
					var y = $(this).next().html();
					y = parseInt(y) - 1;
					$(this).next().html(y);
				}
			});
			tr1td1.appendTo(tr1);
			var tr1td2 = $('<td>', {
				text : year
			}).appendTo(tr1);

			var tr1td3 = $('<td>', {
				text : '+',
				click : function() {
					var y = $(this).prev().html();
					y = parseInt(y) + 1;
					$(this).prev().html(y);
				}
			}).appendTo(tr1);
			tr1.appendTo(table);

			var n = 1;
			for (var i = 1; i <= 4; i++) {
				var tr = $('<tr>');
				for (var m = 1; m <= 3; m++) {
					var td = $('<td>', {
						text : n,
						click : function() {
							var yyyy = $(table).find("tr:first>td:eq(1)").html();
							var cell = $(this).html();
							var v = yyyy + '-' + (cell.length < 2 ? '0' + cell : cell);
							obj.combobox('setValue', v).combobox('hidePanel');
						}
					});
					if (n == month) {
						td.addClass('tdbackground');
					}
					td.appendTo(tr);
					n++;
				}
				tr.appendTo(table);
			}
			table.addClass('mytable cursor');
			table.find('td').hover(function() {
				$(this).addClass('tdbackground');
			}, function() {
				$(this).removeClass('tdbackground');
			});
			table.appendTo(obj.combobox("panel"));
		});
	}
});
