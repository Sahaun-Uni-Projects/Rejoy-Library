package com.example.rejoylibrary;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private final String TAG = "MainActivity";

    private TextView username, balance, txtSubscription;
    private ImageView imgSubscription;
    private DrawerLayout drawer;
    ArrayList<Book> books = new ArrayList<>();
    HashMap<String, Author> authors = new HashMap<>();
    private User user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Layout
        setContentView(R.layout.activity_main);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Rejoy Library");

        // Init nav menu
        drawer = findViewById(R.id.drawerLayout);

        NavigationView navView = findViewById(R.id.navView);
        navView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // DB
        Boolean initRun = false;    // Dev Mode or not

        if (initRun) {
            initDB();
        } else {
            loadDB();

            // Initial Fragment
            //if (savedInstanceState == null) {
                navView.setCheckedItem(R.id.navExplore);
                onNavigationItemSelected(navView.getCheckedItem());
            //}
        }

        // Load user
        loadUserFromAuth();
    }

    private void initDB() {
        addAuthorsToDB();
        addBooksToDB();
    }

    private void loadDB() {
        loadAuthorsFromDB();
        loadBookBuyRecordsFromDB();
        loadBookRentRecordsFromDB();
        loadWalletRecordsFromDB();
        loadBooksFromDB();
    }

    private void loadAuthorsFromDB() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = db.getReference().child("authors");

        dbRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()) {
                    Data.addAuthor(Author.build(snapshot));
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) { }
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    private void loadBookBuyRecordsFromDB() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = db.getReference().child("bookBuyRecords");

        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        dbRef.orderByChild("userEmail").equalTo(email).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()) {
                    Data.addBookBuyRecord(BookBuyRecord.build(snapshot));
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) { }
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    private void loadBookRentRecordsFromDB() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = db.getReference().child("bookRentRecords");

        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        dbRef.orderByChild("userEmail").equalTo(email).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()) {
                    Data.addBookRentRecord(BookRentRecord.build(snapshot));
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) { }
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    private void loadWalletRecordsFromDB() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = db.getReference().child("walletRecords");

        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        dbRef.orderByChild("userEmail").equalTo(email).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()) {
                    Data.addWalletRecord(WalletRecord.build(snapshot));
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) { }
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    private void loadBooksFromDB() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = db.getReference().child("books");

        dbRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()) {
                    Data.addBook(Book.build(snapshot));
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) { }
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void loadUserFromAuth() {
        FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference().child("users")
                .orderByChild("email").equalTo(currUser.getEmail())
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { if (snapshot.exists()) initUser(snapshot); }
                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { if (snapshot.exists()) initUser(snapshot); }
                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) { }
                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });
    }

    private void initUser(DataSnapshot snapshot) {
        user = User.build(snapshot);

        // Components
        username = findViewById(R.id.txtNavUsername);
        balance = findViewById(R.id.txtNavBalance);
        imgSubscription = findViewById(R.id.imgSubscription);
        txtSubscription = findViewById(R.id.txtSubscription);

        // Set nav menu details
        username.setText(user.getName());
        balance.setText(String.format("%.2f", user.getBalance()));

        int img, col;
        if (user.isSubExpired()) {
            img = R.drawable.ic_baseline_warning_24;
            col = getResources().getColor(R.color.c_red3);
            txtSubscription.setText("Not subscribed");
        } else {
            img = R.drawable.ic_baseline_check_box_24;
            col = getResources().getColor(R.color.c_white);
            txtSubscription.setText("Subscribed");
        }
        imgSubscription.setImageResource(img);
        ImageViewCompat.setImageTintList(imgSubscription, ColorStateList.valueOf(col));
        txtSubscription.setTextColor(col);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.navExplore      : setTitle("Explore");        fragment = new FragmentExplore(); break;
            case R.id.navProfile      : setTitle("Profile");        fragment = new FragmentProfile(); break;
            case R.id.navInventory    : setTitle("Inventory");      fragment = new FragmentInventory(); break;
            case R.id.navSubscription : setTitle("Subscription");   fragment = new FragmentSubscription(); break;
            case R.id.navWishlist     : setTitle("Wishlist");       fragment = new FragmentWishlist(); break;
            case R.id.navWallet       : setTitle("Wallet");         fragment = new FragmentWallet(); break;
            case R.id.navContact      : setTitle("Contact Us");     fragment = new FragmentContact(); break;
            case R.id.navSignout      : signOut(); break;
            default: break;
        }

        // Open fragment
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment).commit();
        }

        // Close the drawer
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    private void signOut() {
        // Sign out
        FirebaseAuth.getInstance().signOut();

        // Go to login screen
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void addAuthorsToDB() {
        authors.put("Dante Alighieri", new Author("Dante Alighieri", "Dante Alighieri, probably baptized Durante di Alighiero degli Alighieri and often referred to simply as Dante, was an Italian poet, writer and philosopher. His Divine Comedy, originally called Comedìa (modern Italian: Commedia) and later christened Divina by Giovanni Boccaccio, is widely considered one of the most important poems of the Middle Ages and the greatest literary work in the Italian language."));
        authors.put("Francis Scott Fitzgerald", new Author("Francis Scott Fitzgerald", "Francis Scott Key Fitzgerald (September 24, 1896 – December 21, 1940) was an American novelist, essayist, short story and screenwriter. He was best known for his novels depicting the flamboyance and excess of the Jazz Age—a term he popularized. During his lifetime, he published four novels, four collections of short stories, and 164 short stories. Although he achieved temporary popular success and fortune in the 1920s, Fitzgerald received critical acclaim only after his death, and is now widely regarded as one of the greatest American writers of the 20th century."));
        authors.put("Leo Tolstoy", new Author("Leo Tolstoy", "Count Lev Nikolayevich Tolstoy, usually referred to in English as Leo Tolstoy, was a Russian writer who is regarded as one of the greatest authors of all time. He received nominations for the Nobel Prize in Literature every year from 1902 to 1906 and for the Nobel Peace Prize in 1901, 1902, and 1909. That he never won is a major controversy."));
        authors.put("William Shakespeare", new Author("William Shakespeare", "William Shakespeare (bapt. 26 April 1564 – 23 April 1616) was an English playwright, poet, and actor, widely regarded as the greatest writer in the English language and the world's greatest dramatist. He is often called England's national poet and the \"Bard of Avon\" (or simply \"the Bard\"). His extant works, including collaborations, consist of some 39 plays, 154 sonnets, three long narrative poems, and a few other verses, some of uncertain authorship. His plays have been translated into every major living language and are performed more often than those of any other playwright. They also continue to be studied and reinterpreted."));
        authors.put("Miguel de Cervantes", new Author("Miguel de Cervantes", "Miguel de Cervantes Saavedra was a Spanish writer widely regarded as the greatest writer in the Spanish language and one of the world's pre-eminent novelists. He is best known for his novel Don Quixote, a work often cited as both the first modern novel and one of the pinnacles of world literature."));
        authors.put("Marcel Proust", new Author("Marcel Proust", "Marcel Proust, (born July 10, 1871, Auteuil, near Paris, France—died November 18, 1922, Paris), French novelist, author of À la recherche du temps perdu (1913–27; In Search of Lost Time), a seven-volume novel based on Proust’s life told psychologically and allegorically."));
        authors.put("Herman Melville", new Author("Herman Melville", "Herman Melville, (born August 1, 1819, New York City—died September 28, 1891, New York City), American novelist, short-story writer, and poet, best known for his novels of the sea, including his masterpiece, Moby Dick (1851)."));
        authors.put("Gabriel García Márquez", new Author("Gabriel García Márquez", "Gabriel García Márquez was a Colombian novelist, short-story writer, screenwriter, and journalist, known affectionately as Gabo or Gabito throughout Latin America."));
        authors.put("Homer", new Author("Homer", "Homer was the reputed author of the Iliad and the Odyssey, the two epic poems that are the foundational works of ancient Greek literature. He is regarded as one of the greatest and most influential writers of all time."));
        authors.put("James Joyce", new Author("James Joyce", "James Augustine Aloysius Joyce was an Irish novelist, short story writer, poet, teacher, and literary critic. He contributed to the modernist avant-garde movement and is regarded as one of the most influential and important writers of the 20th century."));

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("authors");
        for (Author author : authors.values()) {
            HashMap<String, String> mp = new HashMap<>();
            mp.put("name", author.getName());
            mp.put("description", author.getDescription());
            DatabaseReference ref = dbRef.push();
            ref.setValue(mp);
            author.setDbKey(ref.getKey());
        }
    }

    private void addBooksToDB() {
        books.add(new Book("Hamlet", authors.get("William Shakespeare"), "hamlet.jpg", "The ghost of the King of Denmark tells his son Hamlet to avenge his murder by killing the new king, Hamlet's uncle. Hamlet feigns madness, contemplates life and death, and seeks revenge. His uncle, fearing for his life, also devises plots to kill Hamlet. The play ends with a duel, during which the King, Queen, Hamlet's opponent and Hamlet himself are all killed. "));
        books.add(new Book("The Divine Comedy", authors.get("Dante Alighieri"), "the_divine_comedy.jpg", "A man, generally assumed to be Dante himself, is miraculously enabled to undertake an ultramundane journey, which leads him to visit the souls in Hell, Purgatory, and Paradise."));
        books.add(new Book("The Great Gatsby", authors.get("Francis Scott Fitzgerald"), "the_great_gatsby.jpg", "Set in the Jazz Age on Long Island, near New York City, the novel depicts first-person narrator Nick Carraway's interactions with mysterious millionaire Jay Gatsby and Gatsby's obsession to reunite with his former lover, Daisy Buchanan."));
        books.add(new Book("War and Peace", authors.get("Leo Tolstoy"), "war_and_peace.jpg", "War and Peace broadly focuses on Napoleon’s invasion of Russia in 1812 and follows three of the most well-known characters in literature: Pierre Bezukhov, the illegitimate son of a count who is fighting for his inheritance and yearning for spiritual fulfillment; Prince Andrei Bolkonsky, who leaves his family behind to fight in the war against Napoleon; and Natasha Rostov, the beautiful young daughter of a nobleman who intrigues both men."));
        books.add(new Book("Don Quixote", authors.get("Miguel de Cervantes"), "don_quixote.jpg", "The Ingenious Gentleman Don Quixote of La Mancha, or just Don Quixote, is a Spanish novel by Miguel de Cervantes. It was originally published in two parts, in 1605 and 1615. A founding work of Western literature, it is often labeled as the first modern novel and is considered one of the greatest works ever written."));
        books.add(new Book("In Search of Lost Time", authors.get("Marcel Proust"), "in_search_of_lost_time.jpg", "In Search of Lost Time, first translated into English as Remembrance of Things Past, and sometimes referred to in French as La Recherche, is a novel in seven volumes by French author Marcel Proust. This early 20th century work is his most prominent, known both for its length and its theme of involuntary memory."));
        books.add(new Book("Moby Dick", authors.get("Herman Melville"), "moby_dick.jpg", "Moby-Dick; or, The Whale is an 1851 novel by American writer Herman Melville. The book is the sailor Ishmael's narrative of the obsessive quest of Ahab, captain of the whaling ship Pequod, for revenge on Moby Dick, the giant white sperm whale that on the ship's previous voyage bit off Ahab's leg at the knee."));
        books.add(new Book("One Hundred Years", authors.get("Gabriel García Márquez"), "one_hundred_years.jpg", "One Hundred Years of Solitude is a landmark 1967 novel by Colombian author Gabriel García Márquez that tells the multi-generational story of the Buendía family, whose patriarch, José Arcadio Buendía, founded the town of Macondo. The novel is often cited as one of the supreme achievements in literature."));
        books.add(new Book("The Odyssey", authors.get("Homer"), "the_odyssey.jpg", "The Odyssey is one of two major ancient Greek epic poems attributed to Homer. It is one of the oldest extant works of literature still read by contemporary audiences. As with the Iliad, the poem is divided into 24 books. It follows the Greek hero Odysseus, king of Ithaca, and his journey home after the Trojan War."));
        books.add(new Book("Ulysses", authors.get("James Joyce"), "ulysses.jpg", "Ulysses is a modernist novel by Irish writer James Joyce. It was first serialized in parts in the American journal The Little Review from March 1918 to December 1920 and then published in its entirety in Paris by Sylvia Beach on 2 February 1922, Joyce's 40th birthday."));

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("books");
        for (Book book : books) {
            HashMap<String, String> mp = new HashMap<>();
            mp.put("name", book.getName());
            mp.put("description", book.getDescription());
            mp.put("coverImage", book.getCoverImage());
            mp.put("authorKey", book.getAuthor().getDbKey());
            DatabaseReference ref = dbRef.push();
            ref.setValue(mp);
            book.setDbKey(ref.getKey());


            BookRecord bookRecord = new BookRecord(
                 book,
                (int) Math.floor(Math.random()*10) + 0,
                (Double) Math.floor(Math.random()*200) + 400.,
                (Double) Math.floor(Math.random()*50) + 50.
            );
            DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference().child("bookRecords");
            ref2.push().setValue(bookRecord.toHashmap());
        }
    }
}