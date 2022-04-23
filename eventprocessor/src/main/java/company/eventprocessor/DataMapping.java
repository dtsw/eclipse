package company.eventprocessor;

public class DataMapping {

    public static String[] getMappedAttributes() {
        String[] attrs = {
        		"msg",
    			"mc_client_address",
				"mc_host",
    	    	"mc_host_class",
    	    	"mc_host_address",
    			"mc_priority",
				"mc_object",
				"mc_object_class",
				"mc_object_uri",
    			"mc_origin",
    			"mc_origin_key",
				"mc_parameter",
				"mc_tool",
    	    	"mc_tool_address",
				"mc_tool_class",
    	    	"mc_ueid",
    	    	
    	    	"hel_heat_organization",
    	    	"hel_heat_group",
    	    	"hel_heat_delay",
    	    	
    	    	"hel_group",
    	    	"hel_group_valid",
    			"hel_srvtyp",
    			
				"recipients",
				"incident_id",
				"fyi",
				"severity",
				"status",
				"time_to_next",
				"date_reception",
				"bppm_cell",
				"host_address",
				"client_address",
				"causes",
				"effects",
				"message",
				"em_alert_type",
				"em_application",
				"em_call_type",
				"em_closed_from_em",
				"em_data_center",
				"em_host_id",
				"em_job_name",
				"em_last_time",
				"em_last_user",
				"em_memname",
				"em_message",
				"em_order_id",
				"em_run_as",
				"em_run_counter",
				"em_send_time",
				"em_severity",
				"em_status",
				"em_sub_application",
				"em_ticket_number",
				"em_user"
				};
        return attrs;
    }

    public static String[] getAttributesForSMS() {
        String[] attrs = {
            "severity",
            "mc_host",
            "status",
            "mc_object",
            "message"};
        return attrs;
    }

    public static String[] getAttributesForMail() {
        String[] attrs = {
            "Recipient",
            "incident_id",
            "message",
            "mc_host",
            "severity",
            "status",
            "date_reception"
        };
        return attrs;
    }
    
    public static String[] getCacheFilterPredicates() {
        String[] attrs = {
            "incident_id",
            "status",
            "severity",
            "mc_priority",
            "fyi",
            "message",
            "recipients"
        };
        return attrs;
    }

}
