ko.bindingHandlers.fadeOutIn = new (function(){
	var self = this;
	self.render = function(elem) {
		$(elem).fadeOut(200, function(){
			$(elem).fadeIn('slow');
		});
	};
	
	self.init = function(elem, valueAccessor, allBindingsAccessor, viewModel) {
		self.render(elem);
    };
    
    self.update = function(elem, valueAccessor, allBindingsAccessor, viewModel) {
    	self.render(elem);
    };
    
	return {init : self.init, update : self.update}; 
})();

ko.bindingHandlers.alertHide = new (function(){
	var self = this;
	self.render = function(elem, valueAccessor) {
        var value = valueAccessor();
        var valueUnwrapped = ko.unwrap(value);
        $(elem).fadeIn(200, function(){
            var fadeOutDelay = valueUnwrapped.fadeOutDelay || 2000;
            $(elem).fadeOut(fadeOutDelay, function () {
                var hide = valueUnwrapped.hide;
                if(hide && typeof hide === 'function') {
                    hide();
                }
            });
        });
	};
	
	self.init = function(elem, valueAccessor, allBindingsAccessor, viewModel) {
		self.render(elem, valueAccessor);
    };

    self.update = function(elem, valueAccessor, allBindingsAccessor, viewModel) {
        self.render(elem, valueAccessor);
    };

    return {init : self.init, update : self.update};
})();

ko.bindingHandlers.formValidate = new (function(){
    var self = this;
	self.init = function(elem, valueAccessor, allBindingsAccessor, viewModel) {
        var value = valueAccessor();
        var valueUnwrapped = ko.unwrap(value);
        $(elem).validate();
        $(elem).on('submit', function (e) {
            var submit = valueUnwrapped.submit;
            if(submit && typeof submit === 'function') {
                submit(elem);
            }
            e.preventDefault();
        });
    };

	return {init : self.init};
})();


ko.bindingHandlers.dataTable = new (function(){
    var self = this;
    self.init = function(elem, valueAccessor, allBindingsAccessor, viewModel) {

        var value = valueAccessor();
        var valueUnwrapped = ko.unwrap(value);
        var data = valueUnwrapped.data;
        if (!data) {
            throw new Error('data é requirido');
        }
        if (!ko.isObservable(data)) {
            throw new Error('data não é ko.observable');
        }
        var options = valueUnwrapped.options || {};
        var table = $(elem).dataTable(options);
        data.subscribe(function(newValues) {
            table.fnClearTable();
            $.each(newValues, function(i, newValue) {
                table.fnAddData(newValue);
            });
        });
    };

    return {init : self.init};
})();