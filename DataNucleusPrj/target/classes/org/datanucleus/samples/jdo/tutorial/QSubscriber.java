package org.datanucleus.samples.jdo.tutorial;

import javax.jdo.query.*;
import org.datanucleus.api.jdo.query.*;

public class QSubscriber extends PersistableExpressionImpl<Subscriber> implements PersistableExpression<Subscriber>
{
    public static final QSubscriber jdoCandidate = candidate("this");

    public static QSubscriber candidate(String name)
    {
        return new QSubscriber(null, name, 5);
    }

    public static QSubscriber candidate()
    {
        return jdoCandidate;
    }

    public static QSubscriber parameter(String name)
    {
        return new QSubscriber(Subscriber.class, name, ExpressionType.PARAMETER);
    }

    public static QSubscriber variable(String name)
    {
        return new QSubscriber(Subscriber.class, name, ExpressionType.VARIABLE);
    }

    public final NumericExpression<Long> id;
    public final StringExpression type;

    public QSubscriber(PersistableExpression parent, String name, int depth)
    {
        super(parent, name);
        this.id = new NumericExpressionImpl<Long>(this, "id");
        this.type = new StringExpressionImpl(this, "type");
    }

    public QSubscriber(Class type, String name, ExpressionType exprType)
    {
        super(type, name, exprType);
        this.id = new NumericExpressionImpl<Long>(this, "id");
        this.type = new StringExpressionImpl(this, "type");
    }
}
