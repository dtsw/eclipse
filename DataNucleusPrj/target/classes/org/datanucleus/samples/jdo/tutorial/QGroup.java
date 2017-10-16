package org.datanucleus.samples.jdo.tutorial;

import javax.jdo.query.*;
import org.datanucleus.api.jdo.query.*;

public class QGroup extends PersistableExpressionImpl<Group> implements PersistableExpression<Group>
{
    public static final QGroup jdoCandidate = candidate("this");

    public static QGroup candidate(String name)
    {
        return new QGroup(null, name, 5);
    }

    public static QGroup candidate()
    {
        return jdoCandidate;
    }

    public static QGroup parameter(String name)
    {
        return new QGroup(Group.class, name, ExpressionType.PARAMETER);
    }

    public static QGroup variable(String name)
    {
        return new QGroup(Group.class, name, ExpressionType.VARIABLE);
    }

    public final StringExpression name;
    public final CollectionExpression subscribers;

    public QGroup(PersistableExpression parent, String name, int depth)
    {
        super(parent, name);
        this.name = new StringExpressionImpl(this, "name");
        this.subscribers = new CollectionExpressionImpl(this, "subscribers");
    }

    public QGroup(Class type, String name, ExpressionType exprType)
    {
        super(type, name, exprType);
        this.name = new StringExpressionImpl(this, "name");
        this.subscribers = new CollectionExpressionImpl(this, "subscribers");
    }
}
