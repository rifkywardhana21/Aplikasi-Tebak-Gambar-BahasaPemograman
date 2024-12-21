package com.example.tebakgambar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DatabaseHelper {

    private final DatabaseReference databaseReference;

    public DatabaseHelper() {
        // Inisialisasi Firebase Realtime Database
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("nilai");
    }

    // Menyimpan atau memperbarui nilai berdasarkan ID
    public void updateNilai(int id, int nilai) {
        Nilai nilaiObj = new Nilai(id, nilai);
        databaseReference.child(String.valueOf(id)).setValue(nilaiObj)
                .addOnSuccessListener(aVoid -> {
                    System.out.println("Data berhasil diperbarui di Firebase");
                })
                .addOnFailureListener(e -> {
                    System.err.println("Gagal memperbarui data: " + e.getMessage());
                });
    }

    // Mendapatkan nilai berdasarkan ID
    public void getNilai(int id, final DataCallback callback) {
        databaseReference.child(String.valueOf(id)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Nilai nilai = dataSnapshot.getValue(Nilai.class);
                    callback.onDataReceived(nilai);
                } else {
                    System.err.println("Data tidak ditemukan untuk ID: " + id);
                    callback.onDataReceived(null);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.err.println("Gagal membaca data: " + databaseError.getMessage());
            }
        });
    }

    // Callback untuk pengambilan data
    public interface DataCallback {
        void onDataReceived(Nilai nilai);
    }
}