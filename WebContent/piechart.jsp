<!Doctype html>
<body>
	<script src="http://d3js.org/d3.v3.min.js"></script>
	<script type="text/javascript" src="https://www.google.com/jsapi?autoload={'modules':[{'name':'visualization','version':'1.1','packages':['corechart']}]}"></script>
	<%-- <jsp:include page="index.jsp" /> --%>
	<script>
	var data= null;
		google.setOnLoadCallback(drawChart);
		
		function drawChart() {
			var usObj;
			
			var arr = [];
			
			d3.json("test1.json", function(us) {
				var keys = d3.keys(us);
				if (keys != undefined && keys != null) {
					var arr1 = [];
					arr1.push("State");
					arr1.push("Incident Count");
					arr.push(arr1);

					keys.forEach(function(d) {
						if (d != undefined && d != null && d != '') {
							var value = [];
							value = d3.values(us[d]);
							var vS="";
							for(v in value){
								vS+=value[v];
							}
							if(vS!=null && vS!=''){
								var arr1 = [];
								arr1.push(d);
								arr1.push(parseInt(vS.toString()));
								arr.push(arr1);
							}
						}
					});
					
					while(data==null){
						data = google.visualization.arrayToDataTable(arr);
		
						var options = {
							title : 'Disaster Chart'
						};
		
						var chart = new google.visualization.PieChart(document
								.getElementById('piechart'));
		
							chart.draw(data, options);
							
					}
					data=null;
				}
				
			});
		}
	</script>
	<div id="piechart" style="width: 900px; height: 500px;"></div>
</body>
</html>