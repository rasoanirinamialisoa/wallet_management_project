CREATE FUNCTION getBalanceSumBetweenDates(accountId INT, startDate TIMESTAMP, endDate TIMESTAMP)
    RETURNS TABLE (total_income DOUBLE PRECISION, total_expense DOUBLE PRECISION) AS $$
BEGIN
    -- Calcul de la somme des entrées (salaire, etc.)
    total_income := COALESCE(SUM(CASE WHEN amount > 0 THEN amount END), 0);

    -- Calcul de la somme des sorties (dépenses, etc.)
    total_expense := COALESCE(SUM(CASE WHEN amount < 0 THEN amount END), 0);

    RETURN;
END;
$$ LANGUAGE plpgsql;
DROP FUNCTION IF EXISTS getBalanceSumBetweenDates(INT, TIMESTAMP, TIMESTAMP);

SELECT * FROM getBalanceSumBetweenDates(1, '2023-01-01', '2023-12-31');
