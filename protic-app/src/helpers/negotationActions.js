function addAction(negotiationId, offeredData, demandedData, actionType, callback) {

    const request = new Request(`/negotiation/${negotiationId}/action`, {
        method: "PUT",
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
            type: actionType,
            offeredData: offeredData,
            demandedData: demandedData
        })
    });

    fetch(request)
        .then(response => {
            !!callback && callback(response);
        });
}

export function create(negotiationId, offeredData, demandedData, callback) {
    return addAction(negotiationId, offeredData, demandedData, "modify", callback);
}

export function cancel(negotiationId, offeredData, demandedData, callback) {
    return addAction(negotiationId, offeredData, demandedData, "cancel", callback);
}

export function accept(negotiationId, offeredData, demandedData, callback) {
    return addAction(negotiationId, offeredData, demandedData, "accept", callback);
}