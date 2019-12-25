	$(document).ready(function() {
		$('#side_accordion').on('hidden shown', function () {
			sidebar.make_active();
            sidebar.update_scroll();
		});
		var lastWindowHeight = $(window).height();
		var lastWindowWidth = $(window).width();
		$(window).on("debouncedresize",function() {
			if($(window).height()!=lastWindowHeight || $(window).width()!=lastWindowWidth){
				lastWindowHeight = $(window).height();
				lastWindowWidth = $(window).width();
				sidebar.update_scroll();
			}
		});
        sidebar.init();
		sidebar.make_active();
		
		sidebar.make_scroll();
		sidebar.update_scroll();
		
		style_sw.init();
		
		$('body').on('touchstart.dropdown', '.dropdown-menu', function (e) { e.stopPropagation(); });
		
	});
    
    sidebar = {
        init: function() {
			// sidebar onload state
			if($(window).width() > 979){
                if(!$('body').hasClass('sidebar_hidden')) {
                    if( $.cookie('sidebar') == "hidden") {
                        $('body').addClass('sidebar_hidden');
                        $('.sidebar_switch').toggleClass('on_switch off_switch').attr('title','Show Sidebar');
                    }
                } else {
                    $('.sidebar_switch').toggleClass('on_switch off_switch').attr('title','Show Sidebar');
                }
            } else {
                $('body').addClass('sidebar_hidden');
                $('.sidebar_switch').removeClass('on_switch').addClass('off_switch');
            }

			sidebar.info_box();
			//* sidebar visibility switch
            $('.sidebar_switch').click(function(){
                $('.sidebar_switch').removeClass('on_switch off_switch');
                if( $('body').hasClass('sidebar_hidden') ) {
                    $.cookie('sidebar', null);
                    $('body').removeClass('sidebar_hidden');
                    $('.sidebar_switch').addClass('on_switch').show();
                    $('.sidebar_switch').attr( 'title', "Hide Sidebar" );
                } else {
                    $.cookie('sidebar', 'hidden');
                    $('body').addClass('sidebar_hidden');
                    $('.sidebar_switch').addClass('off_switch');
                    $('.sidebar_switch').attr( 'title', "Show Sidebar" );
                }
				sidebar.info_box();
				sidebar.update_scroll();
				$(window).resize();
            });
			//* prevent accordion link click
            $('.sidebar .accordion-toggle').click(function(e){e.preventDefault()});
        },
		info_box: function(){
			var s_box = $('.sidebar_info');
			var s_box_height = s_box.actual('height');
			s_box.css({
				'height'        : s_box_height
			});
			$('.push').height(s_box_height);
			$('.sidebar_inner').css({
				'margin-bottom' : '-'+s_box_height+'px',
				'min-height'    : '100%'
			});
        },
		make_active: function() {
			var thisAccordion = $('#side_accordion');
			thisAccordion.find('.accordion-heading').removeClass('sdb_h_active');
			var thisHeading = thisAccordion.find('.accordion-body.in').prev('.accordion-heading');
			if(thisHeading.length) {
				thisHeading.addClass('sdb_h_active');
			}
		},
        make_scroll: function() {
            antiScroll = $('.antiScroll').antiscroll().data('antiscroll');
        },
        update_scroll: function() {
			if($('.antiScroll').length) {
				if( $(window).width() > 979 ){
					$('.antiscroll-inner,.antiscroll-content').height($(window).height() - 40);
				} else {
					$('.antiscroll-inner,.antiscroll-content').height('400px');
				}
				antiScroll.refresh();
			}
        }
    };

	//* submenu
	submenu = {
		init: function() {
			$('.dropdown-menu li').each(function(){
				var $this = $(this);
				if($this.children('ul').length) {
					$this.addClass('sub-dropdown');
					$this.children('ul').addClass('sub-menu');
				}
			});
			
			$('.sub-dropdown').on('mouseenter',function(){
				$(this).addClass('active').children('ul').addClass('sub-open');
			}).on('mouseleave', function() {
				$(this).removeClass('active').children('ul').removeClass('sub-open');
			})
			
		}
	};
	
	style_sw = {
		init: function() {
			if($('.style_switcher').length) {
				$('body').append('<a class="ssw_trigger" href="javascript:void(0)"><i class="icon-cog icon-white"></i></a>');
				var defLink = $('#link_theme').clone();
				
				
				$('input[name=ssw_sidebar]:first,input[name=ssw_layout]:first,input[name=ssw_menu]:first').attr('checked', true);
				
				$(".ssw_trigger").click(function(){
					$(".style_switcher").toggle("fast");
					$(this).toggleClass("active");
					return false;
				});
				
				// colors
				//into main pages by dingjh
				
				// backgrounds
				$('.style_switcher .jQptrn').click(function(){
					$(this).closest('div').find('.style_item').removeClass('style_active');
					$(this).addClass('style_active');
					var style_selected = $(this).attr('title');
					if($(this).hasClass('jQptrn')) { 
						$('body').removeClass('ptrn_a ptrn_b ptrn_c ptrn_d ptrn_e').addClass(style_selected); 
						$("iframe").contents().find("body").removeClass('ptrn_a_iframe ptrn_b_iframe ptrn_c_iframe ptrn_d_iframe ptrn_e_iframe').addClass(style_selected+"_iframe"); 
					};
				});
				//* layout
				$('input[name=ssw_layout]').click(function(){
					var layout_selected = $(this).val();
					$('body').removeClass('gebo-fixed').addClass(layout_selected);
				});
				//* sidebar position
				$('input[name=ssw_sidebar]').click(function(){
					var sidebar_position = $(this).val();
					$('body').removeClass('sidebar_right').addClass(sidebar_position);
					$(window).resize();
				});
			}
		}
	};