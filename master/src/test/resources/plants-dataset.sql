insert into plant_inventory_entry (id, name, description, price)
    values (1, 'Mini excavator', '1.5 Tonne Mini excavator', 150);
insert into plant_inventory_entry (id, name, description, price)
    values (2, 'Mini excavator', '3 Tonne Mini excavator', 200);
insert into plant_inventory_entry (id, name, description, price)
    values (3, 'Midi excavator', '5 Tonne Midi excavator', 250);
insert into plant_inventory_entry (id, name, description, price)
    values (4, 'Midi excavator', '8 Tonne Midi excavator', 300);
insert into plant_inventory_entry (id, name, description, price)
    values (5, 'Maxi excavator', '15 Tonne Large excavator', 400);
insert into plant_inventory_entry (id, name, description, price)
    values (6, 'Maxi excavator', '20 Tonne Large excavator', 450);
insert into plant_inventory_entry (id, name, description, price)
    values (7, 'HS dumper', '1.5 Tonne Hi-Swivel Dumper', 150);
insert into plant_inventory_entry (id, name, description, price)
    values (8, 'FT dumper', '2 Tonne Front Tip Dumper', 180);
insert into plant_inventory_entry (id, name, description, price)
    values (9, 'FT dumper', '2 Tonne Front Tip Dumper', 200);
insert into plant_inventory_entry (id, name, description, price)
    values (10, 'FT dumper', '2 Tonne Front Tip Dumper', 300);
insert into plant_inventory_entry (id, name, description, price)
    values (11, 'FT dumper', '3 Tonne Front Tip Dumper', 400);
insert into plant_inventory_entry (id, name, description, price)
    values (12, 'Loader', 'Hewden Backhoe Loader', 200);
insert into plant_inventory_entry (id, name, description, price)
    values (13, 'D-Truck', '15 Tonne Articulating Dump Truck', 250);
insert into plant_inventory_entry (id, name, description, price)
    values (14, 'D-Truck', '30 Tonne Articulating Dump Truck', 300);

insert into plant_inventory_item (id, plant_info_id, serial_number, equipment_condition)
    values (1, 1, 'A01', 'SERVICEABLE');
insert into plant_inventory_item (id, plant_info_id, serial_number, equipment_condition)
    values (2, 2, 'A02', 'SERVICEABLE');
insert into plant_inventory_item (id, plant_info_id, serial_number, equipment_condition)
    values (3, 3, 'A03', 'UNSERVICEABLEREPAIRABLE');

insert into plant_reservation (id, plant_id, start_date, end_date)
    values (1, 1, '2019-03-22', '2019-03-24');

 insert into maintenance_plan (id, year_of_action, plant_id)
     values (1, 2018, 1);
 insert into maintenance_plan (id, year_of_action, plant_id)
     values (2, 2017, 1);

 insert into maintenance_task (id, description, type_of_work, start_date, end_date, price, plan_id)
     values (1, 'cleaning', 'PREVENTIVE', '2018-06-11', '2018-06-12', 100.0, 1);
 insert into maintenance_task (id, description, type_of_work, start_date, end_date, price, plan_id)
     values (2, 'engine change', 'CORRECTIVE', '2018-06-15', '2018-06-22', 1000.0, 1);
 insert into maintenance_task (id, description, type_of_work, start_date, end_date, price, plan_id)
     values (3, 'cleaning', 'PREVENTIVE', '2017-02-02', '2017-02-02', 60.0, 2);

insert into maintenance_plan_tasks(maintenance_plan_id, tasks_id)
    values (1, 1);
insert into maintenance_plan_tasks(maintenance_plan_id, tasks_id)
    values (2, 2);
