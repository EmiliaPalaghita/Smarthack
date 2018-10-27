package com.example.pemil.smarthack.DataSource;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.*;
public class StockDS {


        private FirebaseDatabase dataBase;
        private DatabaseReference table;


        public StockDS() {
            this.dataBase = FirebaseDatabase.getInstance();
            this.table = dataBase.getReference("Stocks");
        }

        public void sendToDB() {

        }
        


}
