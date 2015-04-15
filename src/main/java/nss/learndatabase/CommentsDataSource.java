package nss.learndatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by nathan on 4/11/15.
 */

// Data Access Object

public class CommentsDataSource {

    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
       MySQLiteHelper.COLUMN_COMMENT };

    public CommentsDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }


    public void close() {
        dbHelper.close();
    }

    public CommentModel createComment(String comment) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_COMMENT, comment);
        long insertId = database.insert(MySQLiteHelper.TABLE_COMMENTS,
                null, values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_COMMENTS,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId,
                null, null, null, null);
        cursor.moveToFirst();
        CommentModel newComment = cursorToComment(cursor);
        cursor.close();
        return newComment;
    }

    public void deleteComment(CommentModel comment) {
        long id = comment.getId();
        System.out.println("Comment Deleted with ID: " + id);
        database.delete(MySQLiteHelper.TABLE_COMMENTS, MySQLiteHelper.COLUMN_ID
        + " = " + id, null);
    }

    public List<CommentModel> getAllComments() {
        List<CommentModel> comments = new ArrayList<CommentModel>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_COMMENTS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            CommentModel comment = cursorToComment(cursor);
            comments.add(comment);
            cursor.moveToNext();
        }

        cursor.close();
        return comments;
    }

    private CommentModel cursorToComment(Cursor cursor) {
        CommentModel comment = new CommentModel();
        comment.setId(cursor.getLong(0));
        comment.setComment(cursor.getString(1));
        return comment;
    }
}
