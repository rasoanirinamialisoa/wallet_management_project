CREATE FUNCTION getCategorySumsBetweenDates(accountId INT, startDate TIMESTAMP, endDate TIMESTAMP)
    RETURNS TABLE (restaurant DOUBLE PRECISION, salaire DOUBLE PRECISION) AS $$
BEGIN
    SELECT
        COALESCE(SUM(CASE WHEN t.amount< 0 AND c.categoryName = 'Restaurant' THEN t.amount END), 0) AS restaurant,
        COALESCE(SUM(CASE WHEN t.amount> 0 AND c.categoryName = 'Salaire' THEN t.amount END), 0) AS salaire
    INTO restaurant, salaire
    FROM Transaction t
             LEFT JOIN Categories c ON t.categoryId = c.categoryId
    WHERE t.accountId = accountId AND t.date BETWEEN startDate AND endDate;

    RETURN;
END;
$$ LANGUAGE plpgsql;
DROP FUNCTION IF EXISTS getCategorySumsBetweenDates(INT, TIMESTAMP, TIMESTAMP);

SELECT * FROM getCategorySumsBetweenDates(1, '2023-01-01', '2023-12-31');
