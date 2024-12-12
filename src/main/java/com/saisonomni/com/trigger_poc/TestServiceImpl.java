//package com.saisonomni.com.trigger_poc;
//
//import jakarta.annotation.PostConstruct;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Service;
//
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.Statement;
//import org.postgresql.PGConnection;
//
//@Service
//public class TestServiceImpl {
//    @Autowired
//    private JdbcTemplate jdbcTemplate;
//    @PostConstruct
//    public void listenToNotifications() {
//        new Thread(() -> {
//            try (Connection conn = DriverManager.getConnection(
//                    "jdbc:postgresql://localhost:5432/your_database",
//                    "your_username", "your_password");
//                 Statement stmt = conn.createStatement()) {
//
//                conn.setAutoCommit(true);
//                stmt.execute("LISTEN sample_channel");
//                System.out.println("Listening to channel: sample_channel");
//
//                while (true) {
//                    var notifications = conn.unwrap(PGConnection.class)
//                            .getNotifications();
//                    if (notifications != null) {
//                        for (var notification : notifications) {
//                            System.out.println("Received notification: " + notification.getParameter());
//                        }
//                    }
//                    Thread.sleep(500);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }).start();
//    }
//    public String helper(){
//        //1. register trigger notify
//        //2. create entities mocking foreign key behaviour
//        //3. send message
//        //4. listen message
//        return "Ok";
//    }
//    public void registerTrigger() {
//        String triggerSQL = """
//            CREATE OR REPLACE FUNCTION notify_event()
//            RETURNS TRIGGER AS $$
//            BEGIN
//                PERFORM pg_notify('sample_channel', row_to_json(NEW)::text);
//                RETURN NEW;
//            END;
//            $$ LANGUAGE plpgsql;
//
//            CREATE TRIGGER sample_trigger
//            AFTER INSERT ON sample_table
//            FOR EACH ROW EXECUTE FUNCTION notify_event();
//        """;
//        jdbcTemplate.execute(triggerSQL);
//        System.out.println("Trigger registered successfully.");
//    }
//}
