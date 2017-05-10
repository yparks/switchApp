package edu.mills.cs115;

import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class DictionaryDatabaseHelperTest extends AndroidTestCase {
    private static final int NUM_OF_DICTIONARY_ENTRIES = 98;
    private static final String TERM = "style";
    private static final String DEFINITION = "<p>See coding conventions.</p>";
    private static final String CATEGORY = "other";
    private static SQLiteDatabase db;

    @Before
    public void setUp() throws Exception {
        RenamingDelegatingContext context =
                new RenamingDelegatingContext(InstrumentationRegistry.getTargetContext(), "_test");
        db = new DictionaryDatabaseUtilities(context).getWritableDatabase();
    }

    @After
    public void takeDown() {
        db.close();
    }

    //Since the database must run twice, once for testing, once for the app, the number
    // should be 196.
    @Test
    public void loadTerms() throws Exception {
        assertEquals(NUM_OF_DICTIONARY_ENTRIES*2, DictionaryDatabaseUtilities.loadTerms());
    }

    // Since the database must run once, the number of terms added will be 98. So, we need
    // to check that at least one term was added; thus, the number of terms should be 99.
    // If the term had not been added, then addTerm would return -1, and so the terms added
    // would be 97.
    @Test
    public void addTerm() throws Exception {
        assertTrue("message",
                DictionaryDatabaseUtilities.addTerm(TERM, DEFINITION, CATEGORY) > NUM_OF_DICTIONARY_ENTRIES);
    }
}