<!DOCTYPE html>
<meta charset="utf-8">
<style>
.state {
	fill: none;
	stroke: #a9a9a9;
	stroke-width: 1;
}

.state:hover {
	fill-opacity: 0.5;
}

#tooltip {
	position: absolute;
	text-align: center;
	padding: 20px;
	margin: 10px;
	font: 12px sans-serif;
	background: lightsteelblue;
	border: 1px;
	border-radius: 2px;
	pointer-events: none;
}

#tooltip h4 {
	margin: 0;
	font-size: 14px;
}

#tooltip {
	background: rgba(0, 0, 0, 0.9);
	border: 1px solid grey;
	border-radius: 5px;
	font-size: 12px;
	width: auto;
	padding: 4px;
	color: white;
	opacity: 0;
}

#tooltip table {
	table-layout: fixed;
}

#tooltip tr td {
	padding: 0;
	margin: 0;
}

#tooltip tr td:nth-child(1) {
	width: 50px;
}

#tooltip tr td:nth-child(2) {
	text-align: center;
}
h2{
text-align:center;
}
</style>
<body>
	<h2 >Disasters in USA</h2>
	<div style="display: table; width: 60%">
		<form action="Home?piechart" method="post">
			<div id="filters1" style="display: table-row; width: 40%">
					<span id="DisasterTypeSpan" style="display: table-cell; width: 33%"> Select Disasters Type:</span>
					<span id="DisasterTypeSelectSpan" style="display: table-cell; width: 33%">
						<select id="DisasterType" name="DisasterType" onchange="javascript:UpdateButton('DisasterType','submitbutton','submitbutton1');">
							<option value="ALL" selected="selected">All Disasters</option>
							<option value="Earthquake">Earthquakes</option>
							<option value="Flood">Flood</option>
							<option value="Hurricane">Hurricane</option>
							<option value="Tornado">Tornado</option>
						</select>
					</span>
					<span id="searchButton" style="display: table-cell; width: 33%">
						<input type="submit" id="submitbutton" name="submitbutton" value="Search Disasters" disabled="disabled" />
					</span>
				</div>
			</form>
			<br />
			<div id="filters2" style="display: table-row; text-align:left; margin-left: 50px;">OR</div>
			<br />
			<form action="Home?barchart" method="post">
				<div id="filters3" style="display: table-row; width: 40%">
					<span id="StateSpan" style="display: table-cell; width: 33%"> Select State : </span>
					<span id="StateSelectSpan" style="display: table-cell; width: 33%">
						<select id="state" name="state" onchange="javascript:UpdateButton('state','submitbutton1','submitbutton');">
								<option value="ALL" selected="selected">ALL States</option>
								<option value="AL">Alabama</option>
								<option value="AK">Alaska</option>
								<option value="AZ">Arizona</option>
								<option value="CA">California</option>
								<option value="CO">Colorado</option>
								<option value="CT">Connecticut</option>
								<option value="DE">Delware</option>
								<option value="FL">Florida</option>
								<option value="GA">Georgia</option>
								<option value="HI">Hawaii</option>
								<option value="ID">Idaho</option>
								<option value="IL">Illinois</option>
								<option value="IN">Indiana</option>
								<option value="IA">Iowa</option>
								<option value="KS">Kansas</option>
								<option value="KY">Kentucky</option>
								<option value="LA">Louisiana</option>
								<option value="ME">Maine</option>
								<option value="MD">Maryland</option>
								<option value="MA">Massachusetts</option>
								<option value="MI">Michingan</option>
								<option value="MN">Minnesota</option>
								<option value="MS">Mississipi</option>
								<option value="MO">Missouri</option>
								<option value="MT">Montana</option>
								<option value="NE">Nebraska</option>
								<option value="NV">Nevada</option>
								<option value="NH">New Hampshire</option>
								<option value="NJ">New Jersey</option>
								<option value="NM">New Mexico</option>
								<option value="NY">New York</option>
								<option value="NC">North Carolina</option>
								<option value="ND">North Dakota</option>
								<option value="OH">Ohio</option>
								<option value="OK">Oklahoma</option>
								<option value="OR">Oregon</option>
								<option value="PA">Pennyslyvania</option>
								<option value="RI">Rhode Island</option>
								<option value="SC">South Carolina</option>
								<option value="SD">South Dakota</option>
								<option value="TN">Tennessee</option>
								<option value="TX">Texas</option>
								<option value="UT">Utah</option>
								<option value="VT">Vermont</option>
								<option value="VA">Virginia</option>
								<option value="WA">Washington</option>
								<option value="WV">West Virginia</option>
								<option value="WI">Wisconsin</option>
								<option value="WY">Wyoming</option>
						</select>
					</span>
					<span id="searchButton" style="display: table-cell; padding-left: 30px; width: 33%">
						<input type="submit" id="submitbutton1" name="submitbutton1" value="Search States" disabled="disabled" />
					</span>
				</div>
			</form>
			<div id="filters4" style="display: table-row; text-align:left; margin-left: 50px; width:390px">OR</div>
			<div id="filters5" style="display: table-row; text-align:left; width:390px;"><a href="Sunburst.html">Disaster Time Analysis</a></div>
		</div>
	<div id="tooltip"></div>
	<!-- div to hold tooltip. -->
	<svg width="900" height="900" id="statesvg" style="float:left;"></svg>
	<!-- svg to hold the map. -->
	<svg width="200" height="600" id="legendsvg" style="float:left;"></svg>
	<script src="uStates.js"></script>
	<!-- creates uStates. -->
	<script src="http://d3js.org/d3.v3.min.js"></script>
	<script>
		function tooltipHtml(n, d) { /* function to create html content string in tooltip div. */
			var y = "";
			if (d != undefined) {
				var array = d.disasters.split(";");
				for ( var x in array) {
					y += "<tr><td>" + array[x] + "</td></tr>"
				}
				return "<h4>" + n + "</h4><table>" + y + "</table>";
			}
		}

		var sampleData = {}; /* Sample random data. */
		var fullList = [ "HI", "AK", "FL", "SC", "GA", "AL", "NC", "TN", "RI",
				"CT", "MA", "ME", "NH", "VT", "NY", "NJ", "PA", "DE", "MD",
				"WV", "KY", "OH", "MI", "WY", "MT", "ID", "WA", "DC", "TX",
				"CA", "AZ", "NV", "UT", "CO", "NM", "OR", "ND", "SD", "NE",
				"IA", "MS", "IN", "IL", "MN", "WI", "MO", "AR", "OK", "KS",
				"LA", "VA" ];

		d3.json("test.json", function(us) {
			var keys = d3.keys(us);
			var TotalCount = 0;
			keys.forEach(function(d) {
				if (fullList.indexOf(d) > -1) {
					var array = d3.values(us[d]);
					var StateCount = 0;
					var arrayStr = "";
					for ( var x in array) {
						arrayStr += array[x] + ";";
						
						var countV = parseInt(array[x].split("-")[1]);
						StateCount += countV;
						TotalCount += StateCount;
					}
					
					var lowV = 6, medV = 1784, highV = 3562;
					sampleData[d] = {
						low : lowV,
						med : medV,
						high : highV,
						stateC : StateCount, 
						disasters : arrayStr,
						color : d3.interpolate("#ffffcc", "#800026")
								(StateCount / TotalCount*24)
					};
				}
			});
			if(sampleData!=undefined)uStates.draw("#statesvg", sampleData, tooltipHtml);
		});

		/* draw states on id #statesvg */
		
		
	</script>
	<script>
		function UpdateButton(selectId, buttonId, disabledButtonId) {
			var bDisable;
			if (document.getElementById(selectId).value == ''
					|| document.getElementById(selectId).value == 'ALL') // change the condition if required.
				bDisable = true;
			else
				bDisable = false;

			document.getElementById(buttonId).disabled = bDisable;
			if(document.getElementById(disabledButtonId).disabled != true){
				document.getElementById(disabledButtonId).disabled = !bDisable;
			}
			
			
		}
	</script>