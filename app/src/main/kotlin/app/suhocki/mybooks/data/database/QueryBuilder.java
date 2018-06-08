package app.suhocki.mybooks.data.database;

import android.arch.persistence.db.SimpleSQLiteQuery;
import android.arch.persistence.db.SupportSQLiteQuery;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

@SuppressWarnings("unused")
public final class QueryBuilder {
    private static final Pattern sLimitPattern =
            Pattern.compile("\\s*\\d+\\s*(,\\s*\\d+\\s*)?");

    public static final String ORDER_TYPE_DESC = "DESC";
    public static final String ORDER_TYPE_ASC = "ASC";
    private boolean mDistinct = false;
    private final String mTable;
    private String[] mColumns = null;
    private String mSelection;
    private Object[] mBindArgs;
    private String mGroupBy = null;
    private String mHaving = null;
    private String mFirstOrderBy = null;
    private String mSecondOrderBy = null;
    private String mFirstOrderType = null;
    private String mSecondOrderType = null;
    private String mLimit = null;
    private String mFilterFieldName = null;
    private String mFilterFrom = null;
    private String mFilterTo = null;

    /**
     * Creates a query for the given table name.
     *
     * @param tableName The table name(s) to query.
     * @return A builder to create a query.
     */
    public static QueryBuilder builder(String tableName) {
        return new QueryBuilder(tableName);
    }

    private QueryBuilder(String table) {
        mTable = table;
    }

    /**
     * Adds DISTINCT keyword to the query.
     *
     * @return this
     */
    public QueryBuilder distinct() {
        mDistinct = true;
        return this;
    }

    /**
     * Sets the given list of columns as the columns that will be returned.
     *
     * @param columns The list of column names that should be returned.
     * @return this
     */
    public QueryBuilder columns(String[] columns) {
        mColumns = columns;
        return this;
    }

    /**
     * Sets the arguments for the WHERE clause.
     *
     * @param selection The list of selection columns
     * @param bindArgs  The list of bind arguments to match against these columns
     * @return this
     */
    public QueryBuilder selection(String selection, Object[] bindArgs) {
        mSelection = selection;
        mBindArgs = bindArgs;
        return this;
    }

    /**
     * Adds a GROUP BY statement.
     *
     * @param groupBy The value of the GROUP BY statement.
     * @return this
     */
    @SuppressWarnings("WeakerAccess")
    public QueryBuilder groupBy(String groupBy) {
        mGroupBy = groupBy;
        return this;
    }

    /**
     * Adds a HAVING statement. You must also provide {@link #groupBy(String)} for this to work.
     *
     * @param having The having clause.
     * @return this
     */
    public QueryBuilder having(String having) {
        mHaving = having;
        return this;
    }

    /**
     * Adds an ORDER BY statement.
     *
     * @param orderBy The order clause.
     * @return this
     */
    public QueryBuilder setFirstOrderBy(String orderBy) {
        mFirstOrderBy = orderBy;
        return this;
    }

    public QueryBuilder setSecondOrderBy(String orderBy) {
        mSecondOrderBy = orderBy;
        return this;
    }

    /**
     * Adds a LIMIT statement.
     *
     * @param limit The limit value.
     * @return this
     */
    public QueryBuilder limit(String limit) {
        if (!isEmpty(limit) && !sLimitPattern.matcher(limit).matches()) {
            throw new IllegalArgumentException("invalid LIMIT clauses:" + limit);
        }
        mLimit = limit;
        return this;
    }

    /**
     * @return a new query
     */
    public SupportSQLiteQuery create() {
        if (isEmpty(mGroupBy) && !isEmpty(mHaving)) {
            throw new IllegalArgumentException(
                    "HAVING clauses are only permitted when using a groupBy clause");
        }
        StringBuilder query = new StringBuilder(120);

        query.append("SELECT ");
        if (mDistinct) {
            query.append("DISTINCT ");
        }
        if (mColumns != null && mColumns.length != 0) {
            appendColumns(query, mColumns);
        } else {
            query.append(" * ");
        }
        query.append(" FROM ");
        query.append(mTable);
        appendClause(query, " WHERE ", mSelection);
        appendClause(query, " AND ", mFilterFieldName);
        appendClause(query, " >= ", mFilterFrom);
        appendClause(query, " AND ", mFilterFieldName);
        appendClause(query, " <= ", mFilterTo);
        appendClause(query, " GROUP BY ", mGroupBy);
        appendClause(query, " HAVING ", mHaving);
        appendClause(query, " ORDER BY ", mFirstOrderBy);
        appendClause(query, " ", mFirstOrderType);
        if (mFirstOrderBy != null && mSecondOrderBy != null) {
            query.append(", ");
            appendClause(query, "", mSecondOrderBy);
        } else {
            appendClause(query, " ORDER BY ", mSecondOrderBy);
        }
        appendClause(query, " ", mSecondOrderType);
        appendClause(query, " LIMIT ", mLimit);

        return new SimpleSQLiteQuery(query.toString(), mBindArgs);
    }

    private static void appendClause(StringBuilder s, String name, String clause) {
        if (!isEmpty(clause)) {
            s.append(name);
            s.append(clause);
        }
    }

    /**
     * Add the names that are non-null in columns to s, separating
     * them with commas.
     */
    private static void appendColumns(StringBuilder s, String[] columns) {
        int n = columns.length;

        for (int i = 0; i < n; i++) {
            String column = columns[i];
            if (i > 0) {
                s.append(", ");
            }
            s.append(column);
        }
        s.append(' ');
    }

    private static boolean isEmpty(String input) {
        return input == null || input.length() == 0;
    }

    public QueryBuilder setFirstOrderType(String orderType) {
        mFirstOrderType = orderType;
        return this;
    }

    public QueryBuilder setSecondOrderType(String orderType) {
        mSecondOrderType = orderType;
        return this;
    }

    public void filter(@NotNull String fieldName, String from, String to) {
        mFilterFieldName = fieldName;
        mFilterFrom = from;
        mFilterTo = to;
    }
}
