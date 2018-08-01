package zserio.ast;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import zserio.antlr.ZserioParserTokenTypes;
import zserio.antlr.util.BaseTokenAST;
import zserio.antlr.util.ParserException;
import zserio.tools.ZserioToolPrinter;

/**
 * AST node for rpc types.
 *
 * Rpc types are Zserio types as well.
 */
public class RpcType extends CompoundType
{
    @Override
    public Package getPackage()
    {
        return pkg;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public void callVisitor(ZserioTypeVisitor visitor)
    {
        visitor.visitRpcType(this);
    }

    public ZserioType getRequestType()
    {
        return requestType;
    }

    public ZserioType getResponseType()
    {
        return responseType;
    }

    /**
     * Sets package for the rpc.
     *
     * This method is called by code generated by ANTLR using TypeEvaluator.g.
     *
     * @param pkg Package to set.
     */
    public void setPackage(Package pkg)
    {
        this.pkg = pkg;
    }

    @Override
    protected boolean evaluateChild(BaseTokenAST child) throws ParserException
    {
        switch (child.getType())
        {
        case ZserioParserTokenTypes.ID:
            if (!(child instanceof IdToken))
                return false;
            name = child.getText();
            break;

        default:
            if (!(child instanceof ZserioType))
                return false;
            if (requestType == null)
                requestType = (ZserioType)child;
            else
                responseType = (ZserioType)child;
            break;
        }

        return true;
    }

    @Override
    protected void evaluate() throws ParserException
    {
        evaluateHiddenDocComment(this);
        setDocComment(getHiddenDocComment());
    }

    private Package pkg;
    private String name;
    private ZserioType requestType;
    private ZserioType responseType;
}