var names; 
var teams;
var reverseTeams= {};
var teamList;
var badges;
var badgeTypes;

$(document).ready(getData)
function keys(x) {
    return $.map(x, function(v,k) { return k });
}
function values(x) {
    return $.map(x, function(v,k) { return v });
}
function getData() {
    $.getJSON('/data', function(data) {
	names= data.teamData['team-names'];
	teams= keys(names);
	$.each(teams, function(v,k) { reverseTeams[v]= k });
	teamList= values(names).sort();
	badges= data.badgeData;
	badgeTypes= keys(badges);
	renderData();
//	showBadgeData('http://www.nhl.com/team-25');
    });
}
function renderData() {
    $('#teams').empty();
    $.each(teamList, function(i, v) {
	$('#teams').append('<option value="'+reverseTeams[v]+'">'+v+'</option>');
    })
}
function showBadgeData(teamID) {
    var b= [];
    $.each(badgeTypes, function(i,x) { 
//	return $.grep(badges[x], function(v,k) { v.team == teamID });
	return 
    });
    b= $.grep(b, function(x,i) { x.team == teamID });
    alert(b);
}
