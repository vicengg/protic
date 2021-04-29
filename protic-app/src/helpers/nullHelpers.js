export function emptyIfNull(value) {
    return !value ? "" : value;
}

export function nullIfEmpty(value) {
    return value === "" ? null : value;
}