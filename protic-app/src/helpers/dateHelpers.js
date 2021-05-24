const months = ["enero", "febrero", "marzo", "abril", "mayo", "junio", "julio", "agosto", "septiembre", "octubre", "noviembre", "diciembre"];

export function formatDate(date) {
    return `el ${date.getDate()} de ${months[date.getMonth()]} a las ${date.getHours()}:${date.getMinutes()}`
}

export function formatDateTimeAgo(date) {
    const time = (new Date()).getTime() - date.getTime();
    if (time < 60 * 1000) {
        return "ahora"
    } else if (time >= 60 * 1000 && time < 2 * 60 * 1000) {
        return `hace 1 minuto`;
    } else if (time >= 2 * 60 * 1000 && time < 60 * 60 * 1000) {
        return `hace ${Math.trunc(time / (60 * 1000))} minutos`;
    } else if (time >= 60 * 60 * 1000 && time < 2 * 60 * 60 * 1000) {
        return `hace 1 hora`;
    } else if (time >= 2 * 60 * 60 * 1000 && time < 24 * 60 * 60 * 1000) {
        return `hace ${Math.trunc(time / (60 * 60 * 1000))} horas`;
    } else if (time >= 24 * 60 * 60 * 1000 && time < 2 * 24 * 60 * 60 * 1000) {
        return `hace 1 día`;
    } else if (time >= 2 * 24 * 60 * 60 * 1000 && time < 30 * 24 * 60 * 60 * 1000) {
        return `hace ${Math.trunc(time / (24 * 60 * 60 * 1000))} días`;
    } else {
        return formatDate(date);
    }
}