function [journey, new_case] = revise(retrieved_cases, new_case, new_price)
    
    retrieved_codes = retrieved_cases{:,1};
    code = str2double('-');
        
    while isnan(code) || fix(code) ~= code || ismember(code, retrieved_codes) == 0
        fprintf('From the retrieved cases, which is the one that better matches your journey?\n');
        code = str2double(input('Journey Code: ','s'));
    end
    
    journey = fix(code);
    
    fprintf('\nUpdate your journey price with the new estimated value? (y/n)\n');
    option = input('Option: ', 's');

    if option == 'y' || option == 'Y'
        new_case.price = new_price;
    end
end

