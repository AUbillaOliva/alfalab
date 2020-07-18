package cl.alfa.alfalab.utils.databases;

        import android.provider.BaseColumns;

public class OrdersContract {
    public static final class OrdersEntry implements BaseColumns {
        public static final String TABLE_NAME = "orders";
        public static final String COLUMN_ORDER_ID = "order_id";
        public static final String COLUMN_ORDER_NUMBER = "order_number";
        public static final String COLUMN_ORDER_COMMENTARY = "order_commentary";
        public static final String COLUMN_ORDER_RESPONSIBLE = "order_responsible";
        public static final String COLUMN_ORDER_CHECKIN = "order_checkin";
        public static final String COLUMN_ORDER_CHECKOUT = "order_checkout";
        public static final String COLUMN_ORDER_TYPE = "order_type";
        public static final String COLUMN_ORDER_STATUS = "order_status";
        public static final String COLUMN_ORDER_PRICE = "order_price";
        public static final String COLUMN_ORDER_CLIENT_ID = "order_client_id";
    }
}
