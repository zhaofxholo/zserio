package functions_error;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import test_utils.ZserioErrors;

public class FunctionsErrorTest
{
    @BeforeClass
    public static void readZserioErrors() throws IOException
    {
        zserioErrors = new ZserioErrors();
    }

    @Test
    public void compoundFieldNotAvailable()
    {
        final String error = "compound_field_not_available_error.zs:17:23: Unresolved symbol " +
                "'header2.hasOptional' within expression scope! Found in function 'hasOptional2' called " +
                "from here:";
        assertTrue(zserioErrors.isPresent(error));
    }

    @Test
    public void differentScopes()
    {
        final String error = "different_scopes_error.zs:20:30: Unresolved symbol " +
                "'val3' within expression scope! Found in function 'getValue' called from here:";
        assertTrue(zserioErrors.isPresent(error));
    }

    @Test
    public void fieldNotAvailable()
    {
        final String error = "field_not_available_error.zs:17:16: Unresolved symbol " +
                "'hasSpecialData' within expression scope! Found in function 'hasSpecial' called from here:";
        assertTrue(zserioErrors.isPresent(error));
    }

    @Test
    public void recursive()
    {
        final String error = "recursive_error.zs:11:62: Unresolved symbol 'getValue' within expression scope!";
        assertTrue(zserioErrors.isPresent(error));
    }

    @Test
    public void wrongReturnType()
    {
        final String error = "wrong_return_type_error.zs:9:16: Wrong type of value expression (integer " +
                "cannot be assigned to bool)!";
        assertTrue(zserioErrors.isPresent(error));
    }

    private static ZserioErrors zserioErrors;
}
