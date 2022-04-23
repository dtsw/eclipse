package company.eventprocessor.entity;

import java.util.Date;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jms.Message;

@PersistenceCapable
public class Event {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.NATIVE)
	Long id;

	public Event() {
	}

	public Event(Message message) {
		super();
		try {
			eventClass = message.getStringProperty("CLASS");
			severity = message.getStringProperty("severity");
			status = message.getStringProperty("status");
			msg = message.getStringProperty("msg");
			mc_host = message.getStringProperty("mc_host");
			mc_host_address = message.getStringProperty("mc_host_address");
			mc_object = message.getStringProperty("mc_object");
			mc_ueid = message.getStringProperty("mc_ueid");

			long num = Long.parseLong(message.getStringProperty("date_reception"));
			Date date = new Date(num * 1000);
			date_reception = date;

			mc_client_address = message.getStringProperty("mc_client_address");

			mc_host_class = message.getStringProperty("mc_host_class");

			mc_priority = message.getStringProperty("mc_priority");

			mc_object_class = message.getStringProperty("mc_object_class");
			mc_object_uri = message.getStringProperty("mc_object_uri");
			mc_origin = message.getStringProperty("mc_origin");
			mc_origin_key = message.getStringProperty("mc_origin_key");
			mc_parameter = message.getStringProperty("mc_parameter");
			mc_tool = message.getStringProperty("mc_tool");
			mc_tool_address = message.getStringProperty("mc_tool_address");
			mc_tool_class = message.getStringProperty("mc_tool_class");

			hel_heat_organization = message.getStringProperty("hel_heat_organization");
			hel_heat_group = message.getStringProperty("hel_heat_group");
			hel_heat_delay = message.getStringProperty("hel_heat_delay");

			hel_group = message.getStringProperty("hel_group");
			hel_group_valid = message.getStringProperty("hel_group_valid");
			hel_srvtyp = message.getStringProperty("hel_srvtyp");

			recipients = message.getStringProperty("recipients");
			incident_id = message.getStringProperty("incident_id");
			fyi = message.getStringProperty("fyi");
			time_to_next = message.getStringProperty("time_to_next");

			bppm_cell = message.getStringProperty("bppm_cell");
			host_address = message.getStringProperty("host_address");
			client_address = message.getStringProperty("client_address");
			causes = message.getStringProperty("causes");
			effects = message.getStringProperty("effects");

			em_alert_type = message.getStringProperty("em_alert_type");
			em_application = message.getStringProperty("em_application");
			em_call_type = message.getStringProperty("em_call_type");
			em_closed_from_em = message.getStringProperty("em_closed_from_em");
			em_data_center = message.getStringProperty("em_data_center");
			em_host_id = message.getStringProperty("em_host_id");
			em_job_name = message.getStringProperty("em_job_name");
			em_last_time = message.getStringProperty("em_last_time");
			em_last_user = message.getStringProperty("em_last_user");
			em_memname = message.getStringProperty("em_memname");
			em_message = message.getStringProperty("em_message");
			em_order_id = message.getStringProperty("em_order_id");
			em_run_as = message.getStringProperty("em_run_as");
			em_run_counter = message.getStringProperty("em_run_counter");
			em_send_time = message.getStringProperty("em_send_time");
			em_severity = message.getStringProperty("em_severity");
			em_status = message.getStringProperty("em_status");
			em_sub_application = message.getStringProperty("em_sub_application");
			em_ticket_number = message.getStringProperty("em_ticket_number");
			em_user = message.getStringProperty("em_user");
//            DateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
//            format.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
//            String formatted = format.format(date);
//            System.out.println(formatted);
//            format.setTimeZone(TimeZone.getTimeZone("Europe/Zurich"));
//            formatted = format.format(date);
//            System.out.println(formatted);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	String eventClass;
	@Column(length = 1024)
	String msg;
	
	Date date_reception;
	String severity;
	String mc_host;
	String mc_host_address;
	String status;
	String mc_object;
	String mc_ueid;
	String mc_client_address;
	String mc_host_class;
	String mc_priority;
	String mc_object_class;
	String mc_object_uri;
	String mc_origin;
	String mc_origin_key;
	String mc_parameter;
	String mc_tool;
	String mc_tool_address;
	String mc_tool_class;
	String hel_heat_organization;
	String hel_heat_group;
	String hel_heat_delay;
	String hel_group;
	String hel_group_valid;
	String hel_srvtyp;
	String recipients;
	String incident_id;
	String fyi;
	String time_to_next;
	String bppm_cell;
	String host_address;
	String client_address;
	String causes;
	String effects;
	String message;
	String em_alert_type;
	String em_application;
	String em_call_type;
	String em_closed_from_em;
	String em_data_center;
	String em_host_id;
	String em_job_name;
	String em_last_time;
	String em_last_user;
	String em_memname;
	String em_message;
	String em_order_id;
	String em_run_as;
	String em_run_counter;
	String em_send_time;
	String em_severity;
	String em_status;
	String em_sub_application;
	String em_ticket_number;
	String em_user;

}
