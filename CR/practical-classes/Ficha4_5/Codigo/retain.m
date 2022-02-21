function [case_library] = retain(case_library, new_case, journey_index)

    journey = max(case_library{:, 1}) + 1;
    hotel = case_library{journey_index, 10};

    new_row = {journey, new_case.holiday_type, new_case.price, new_case.number_persons, ...
                        new_case.region, new_case.transportation, new_case.duration, new_case.season, ...
                        new_case.accommodation, hotel};

    case_library = [case_library; new_row];
            
    fprintf('Add the new case to the library? (y/n)\n');
    option = input('Option: ', 's');

    if option == 'y' || option == 'Y'    
        
        load('regions');
        
        keys = regions_positions.keys;
        keys{size(keys,2) + 1} = new_case.region;
        
        values = regions_positions.values;
        values{size(values,2) + 1} = new_case.latlon;
        
        regions_positions = containers.Map(keys, values);
    
        save('regions', 'regions_positions')
        
        writetable(case_library, 'TravelCaseBase.csv'); 
    end
end

