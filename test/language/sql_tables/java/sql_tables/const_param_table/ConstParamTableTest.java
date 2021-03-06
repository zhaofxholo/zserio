package sql_tables.const_param_table;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import test_utils.FileUtil;
import test_utils.JdbcUtil;

import sql_tables.IParameterProvider;
import sql_tables.TestDb;

import zserio.runtime.ZserioError;
import zserio.runtime.SqlDatabase.Mode;

public class ConstParamTableTest
{
    @BeforeClass
    public static void init()
    {
        JdbcUtil.registerJdbc();
    }

    @Before
    public void setUp() throws IOException, URISyntaxException, SQLException
    {
        FileUtil.deleteFileIfExists(file);
        database = new TestDb(file.toString());
        database.createSchema();
    }

    @After
    public void tearDown() throws SQLException
    {
        if (database != null)
        {
            database.close();
            database = null;
        }
    }

    @Test
    public void deleteTable() throws SQLException
    {
        assertTrue(isTableInDb());

        final ConstParamTable testTable = database.getConstParamTable();
        testTable.deleteTable();
        assertFalse(isTableInDb());

        testTable.createTable();
        assertTrue(isTableInDb());
    }

    @Test
    public void readWithoutCondition() throws SQLException, URISyntaxException, IOException, ZserioError
    {
        final ConstParamTable testTable = database.getConstParamTable();

        final List<ConstParamTableRow> writtenRows = new ArrayList<ConstParamTableRow>();
        fillConstParamTableRows(writtenRows);
        testTable.write(writtenRows);

        final List<ConstParamTableRow> readRows = testTable.read(null);
        checkConstParamTableRows(writtenRows, readRows);
    }

    @Test
    public void readWithCondition() throws SQLException, URISyntaxException, IOException, ZserioError
    {
        final ConstParamTable testTable = database.getConstParamTable();

        final List<ConstParamTableRow> writtenRows = new ArrayList<ConstParamTableRow>();
        fillConstParamTableRows(writtenRows);
        testTable.write(writtenRows);

        final String condition = "name='Name1'";
        final List<ConstParamTableRow> readRows = testTable.read(null, condition);

        assertEquals(1, readRows.size());

        final int expectedRowNum = 1;
        final ConstParamTableRow readRow = readRows.get(0);
        checkConstParamTableRow(writtenRows.get(expectedRowNum), readRow);
    }

    @Test
    public void update() throws SQLException, URISyntaxException, IOException, ZserioError
    {
        final ConstParamTable testTable = database.getConstParamTable();

        final List<ConstParamTableRow> writtenRows = new ArrayList<ConstParamTableRow>();
        fillConstParamTableRows(writtenRows);
        testTable.write(writtenRows);

        final int updateRowId = 3;
        final ConstParamTableRow updateRow = createConstParamTableRow(updateRowId, "UpdatedName");
        final String updateCondition = "id=" + updateRowId;
        testTable.update(updateRow, updateCondition);

        final List<ConstParamTableRow> readRows = testTable.read(null, updateCondition);
        assertEquals(1, readRows.size());

        final ConstParamTableRow readRow = readRows.get(0);
        checkConstParamTableRow(updateRow, readRow);
    }

    private static void fillConstParamTableRows(List<ConstParamTableRow> rows)
    {
        for (int id = 0; id < NUM_CONST_PARAM_TABLE_ROWS; ++id)
        {
            rows.add(createConstParamTableRow(id, "Name" + id));
        }
    }

    private static ConstParamTableRow createConstParamTableRow(int id, String name)
    {
        final ConstParamTableRow row = new ConstParamTableRow();
        row.setId(id);
        row.setName(name);
        final ParameterizedBlob parameterizedBlob = new ParameterizedBlob(PARAMETERIZED_BLOB_PARAM,
                PARAMETERIZED_BLOB_VALUE);
        row.setBlob(parameterizedBlob);

        return row;
    }

    private static void checkConstParamTableRows(List<ConstParamTableRow> rows1,
            List<ConstParamTableRow> rows2)
    {
        assertEquals(rows1.size(), rows2.size());
        for (int i = 0; i < rows1.size(); ++i)
            checkConstParamTableRow(rows1.get(i), rows2.get(i));
    }

    private static void checkConstParamTableRow(ConstParamTableRow row1, ConstParamTableRow row2)
    {
        assertEquals(row1.getId(), row2.getId());
        assertEquals(row1.getName(), row2.getName());
        assertEquals(row1.getBlob(), row2.getBlob());
    }

    private boolean isTableInDb() throws SQLException
    {
        // check if database does contain table
        final String sqlQuery = "SELECT name FROM sqlite_master WHERE type='table' AND name='" + TABLE_NAME +
                "'";

        final PreparedStatement statement = database.prepareStatement(sqlQuery);
        try
        {
            final ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next())
                return false;

            // read table name
            final String tableName = resultSet.getString(1);
            if (resultSet.wasNull() || !tableName.equals(TABLE_NAME))
                return false;
        }
        finally
        {
            statement.close();
        }

        return true;
    }

    private static final String TABLE_NAME = "constParamTable";

    private static final int    PARAMETERIZED_BLOB_VALUE = 0xABCD;
    private static final int    PARAMETERIZED_BLOB_PARAM = 2;
    private static final int    NUM_CONST_PARAM_TABLE_ROWS = 5;
    private static final String FILE_NAME = "const_param_table_test.sqlite";

    private final File file = new File(FILE_NAME);
    private TestDb database = null;
}
