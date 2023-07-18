$(document).ready(function(){
	
    $(".phone").keypress(function(event){
        event = event || window.event;
        if (event.charCode && event.charCode!=0 && event.charCode!=46 && (event.charCode < 48 || event.charCode > 57) )
            return false;
    });
	
    $(".link-delete").click(function( event ) {
        event.preventDefault();
        var id = $(this).data('id');
        var title = $(this).data('title');
        var type = $(this).data('type');
        $('.delete-title-'+type).text(title);
        $('.delete-id-'+type).val(id);
    });

    first=0;
    UIkit.util.on('#sortable-item', 'moved', function () {
        first++;
        var children 	= this.children;
        var num  		= this.children.length;
        var order		= [ ];
        for (var i = 0; i < num; i++) {
            order.push(children[i].id);
        }
        var order = order.join('|');
        document.getElementById('sortable-category').value = order;
        if(first==1){UIkit.toggle('#sortable-form').toggle();}
    });
	
	var dateToday = new Date();
	var dayToday = dateToday.getDay();
	if(dayToday==0){dayToday=7;}
	var nextMonday = 8 - dayToday;
	
	//console.log(dayToday);
	//console.log(nextMonday);
	
	$(".datepicker").datepicker({
		dateFormat: 'yy-mm-dd',
		firstDay: 1,
	});

	$(".datepicker-nextmonday").datepicker({
		dateFormat: 'yy-mm-dd',
		firstDay: 1,
		minDate: nextMonday,
		showOtherMonths: true,
		showWeek: true,
	});

	$(".datepicker-nextday").datepicker({
		dateFormat: 'yy-mm-dd',
		firstDay: 1,
		minDate: 1,
		showOtherMonths: true,
		showWeek: true,
	});
	
	$(".input-search").keyup(function(){
        var search = $(this).val();
		var type = $(this).data('type');
        if(search != ""){
            $.ajax({
                url: '/api/'+type,
                type: 'post',
                data: {search:search},
                dataType: 'json',
                success:function(response){				
					var entries = response.entries;
					console.log(entries);
					$(".input-search-result-"+type).empty();
					$(".input-search-result-"+type).attr("hidden",false);
					$.each( entries, function( key, value ) {
						var liClass = "";
						if(value.id==0){
							liClass = "footer";
						}
						$(".input-search-result-"+type).append("<li class='"+liClass+"' value='"+value.id+"'>"+value.title+"</li>");
					});
					
					$(".input-search-result-"+type+" li").bind("click",function(){
						if(this.value>0){
							$("input[name='"+type+"-id']").val(this.value);
							$("input[name='"+type+"']").val("");
							$(".input-search-result-"+type).empty();
							$(".input-search-title-"+type+" span").text($(this).text());
							$(".input-search-title-"+type).attr("hidden",false);
							$(".input-search-container-"+type).attr("hidden",true);
							$(".input-search-result-"+type).attr("hidden",true);
						}else{
							$("input[name='"+type+"-id']").val("");
							$("input[name='"+type+"']").val("");							
							$(".input-search-result-"+type).empty();
							$(".input-search-result-"+type).attr("hidden",true);
						}
                    });
                }
            });
        }
    });	
	
	$(".input-search-change").click(function( event ) {
        event.preventDefault();
        var type = $(this).data('type');
		$(".input-search-title-"+type).attr("hidden",true);
		$(".input-search-container-"+type).attr("hidden",false);
		$("input[name='"+type+"-id']").val("");
		$(".input-search-title-"+type+" span").text("");
    });
	

	
});