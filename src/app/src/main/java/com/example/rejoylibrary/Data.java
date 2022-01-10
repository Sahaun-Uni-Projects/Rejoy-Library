package com.example.rejoylibrary;

import java.util.HashMap;

public class Data {
    private static class DataBook {
        private static HashMap<String, Book> mp = new HashMap<>();

        public static void add(Book obj) {
            String key = obj.getDbKey();
            if (!mp.containsKey(key)) {
                mp.put(key, obj);
            }
        }

        public static boolean containsKey(String key) {
            return mp.containsKey(key);
        }

        public static Book get(String key) {
            Book obj = null;
            if (mp.containsKey(key)) obj = mp.get(key);
            return obj;
        }

        public static HashMap<String, Book> getAll() {
            HashMap<String, Book> m = new HashMap<>();
            for (String key : mp.keySet()) {
                m.put(key, mp.get(key));
            }
            return m;
        }
    }

    private static class DataAuthor {
        private static HashMap<String, Author> mp = new HashMap<>();

        public static void add(Author obj) {
            String key = obj.getDbKey();
            if (!mp.containsKey(key)) {
                mp.put(key, obj);
            }
        }

        public static boolean containsKey(String key) {
            return mp.containsKey(key);
        }

        public static Author get(String key) {
            Author obj = null;
            if (mp.containsKey(key)) obj = mp.get(key);
            return obj;
        }

        public static HashMap<String, Author> getAll() {
            HashMap<String, Author> m = new HashMap<>();
            for (String key : mp.keySet()) {
                m.put(key, mp.get(key));
            }
            return m;
        }
    }

    private static class DataBookRecord {
        private static HashMap<String, BookRecord> mp = new HashMap<>();

        public static void add(BookRecord obj) {
            String key = obj.getDbKey();
            if (!mp.containsKey(key)) {
                mp.put(key, obj);
            }
        }

        public static boolean containsKey(String key) {
            return mp.containsKey(key);
        }

        public static BookRecord get(String key) {
            BookRecord obj = null;
            if (mp.containsKey(key)) obj = mp.get(key);
            return obj;
        }

        public static HashMap<String, BookRecord> getAll() {
            HashMap<String, BookRecord> m = new HashMap<>();
            for (String key : mp.keySet()) {
                m.put(key, mp.get(key));
            }
            return m;
        }
    }

    private static class DataBookBuyRecord {
        private static HashMap<String, BookBuyRecord> mp = new HashMap<>();

        public static void add(BookBuyRecord obj) {
            String key = obj.getDbKey();
            if (!mp.containsKey(key)) {
                mp.put(key, obj);
            }
        }

        public static boolean containsKey(String key) {
            return mp.containsKey(key);
        }

        public static BookBuyRecord get(String key) {
            BookBuyRecord obj = null;
            if (mp.containsKey(key)) obj = mp.get(key);
            return obj;
        }

        public static HashMap<String, BookBuyRecord> getAll() {
            HashMap<String, BookBuyRecord> m = new HashMap<>();
            for (String key : mp.keySet()) {
                m.put(key, mp.get(key));
            }
            return m;
        }
    }

    private static class DataBookRentRecord {
        private static HashMap<String, BookRentRecord> mp = new HashMap<>();

        public static void add(BookRentRecord obj) {
            String key = obj.getDbKey();
            if (!mp.containsKey(key)) {
                mp.put(key, obj);
            }
        }

        public static boolean containsKey(String key) {
            return mp.containsKey(key);
        }

        public static BookRentRecord get(String key) {
            BookRentRecord obj = null;
            if (mp.containsKey(key)) obj = mp.get(key);
            return obj;
        }

        public static HashMap<String, BookRentRecord> getAll() {
            HashMap<String, BookRentRecord> m = new HashMap<>();
            for (String key : mp.keySet()) {
                m.put(key, mp.get(key));
            }
            return m;
        }
    }

    private static class DataWalletRecord {
        private static HashMap<String, WalletRecord> mp = new HashMap<>();

        public static void add(WalletRecord obj) {
            String key = obj.getDbKey();
            if (!mp.containsKey(key)) {
                mp.put(key, obj);
            }
        }

        public static boolean containsKey(String key) {
            return mp.containsKey(key);
        }

        public static WalletRecord get(String key) {
            WalletRecord obj = null;
            if (mp.containsKey(key)) obj = mp.get(key);
            return obj;
        }

        public static HashMap<String, WalletRecord> getAll() {
            HashMap<String, WalletRecord> m = new HashMap<>();
            for (String key : mp.keySet()) {
                m.put(key, mp.get(key));
            }
            return m;
        }
    }

    public static void init() {
        DataBook.mp = new HashMap<>();
        DataAuthor.mp = new HashMap<>();
        DataBookRecord.mp = new HashMap<>();
        DataBookBuyRecord.mp = new HashMap<>();
        DataBookRentRecord.mp = new HashMap<>();
        DataWalletRecord.mp = new HashMap<>();
    }

    public static void addBook(Book obj) {
        DataBook.add(obj);
    }

    public static Book getBook(String key) {
        return DataBook.get(key);
    }

    public static HashMap<String, Book> getBooks() {
        return DataBook.getAll();
    }

    public static void addAuthor(Author obj) {
        DataAuthor.add(obj);
    }

    public static Author getAuthor(String key) {
        return DataAuthor.get(key);
    }

    public static HashMap<String, Author> getAuthors() {
        return DataAuthor.getAll();
    }

    public static void addBookRecord(BookRecord obj) {
        DataBookRecord.add(obj);
    }

    public static BookRecord getBookRecord(String key) {
        return DataBookRecord.get(key);
    }

    public static HashMap<String, BookRecord> getBookRecords() {
        return DataBookRecord.getAll();
    }

    public static BookBuyRecord getBookBuyRecord(String key) {
        return DataBookBuyRecord.get(key);
    }

    public static void addBookBuyRecord(BookBuyRecord obj) {
        DataBookBuyRecord.add(obj);
    }

    public static boolean containsKey(String key) {
        return DataBookBuyRecord.containsKey(key);
    }

    public static HashMap<String, BookBuyRecord> getBookBuyRecords() {
        return DataBookBuyRecord.getAll();
    }

    public static BookRentRecord getBookRentRecord(String key) {
        return DataBookRentRecord.get(key);
    }

    public static void addBookRentRecord(BookRentRecord obj) {
        DataBookRentRecord.add(obj);
    }

    public static HashMap<String, BookRentRecord> getBookRentRecords() {
        return DataBookRentRecord.getAll();
    }

    public static void addWalletRecord(WalletRecord obj) {
        DataWalletRecord.add(obj);
    }

    public static WalletRecord getWalletRecord(String key) {
        return DataWalletRecord.get(key);
    }

    public static HashMap<String, WalletRecord> getWalletRecords() {
        return DataWalletRecord.getAll();
    }
}
