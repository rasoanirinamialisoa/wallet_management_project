CREATE FUNCTION getCategorySumsBetweenDates(accountId INT, startDate TIMESTAMP, endDate TIMESTAMP)
    RETURNS TABLE (restaurant DOUBLE PRECISION, salaire DOUBLE PRECISION) AS $$
BEGIN
    SELECT
        COALESCE(SUM(CASE WHEN t.amount < 0 AND c.category_name = 'Restaurant' THEN t.amount END), 0) AS restaurant,
        COALESCE(SUM(CASE WHEN t.amount > 0 AND c.category_name = 'Salaire' THEN t.amount END), 0) AS salaire
    INTO restaurant, salaire
    FROM Transactions t
             LEFT JOIN Categories c ON t.category_id = c.category_id
    WHERE t.account_id = accountId AND t.date BETWEEN startDate AND endDate;

    RETURN;
END;
$$ LANGUAGE plpgsql;
