import { useState, useEffect } from 'react';

export const useResize = (myRef) => {

    const [dimensions, setDimensions] = useState({ width: 0, height: 0 })

    useEffect(() => {
        const handleResize = () => {
            setDimensions({
                width: myRef.current.offsetWidth,
                height: myRef.current.offsetHeight
            })
        }

        if (myRef.current) {
            setDimensions({
                width: myRef.current.offsetWidth,
                height: myRef.current.offsetHeight
            })
        }

        window.addEventListener("resize", handleResize)

        return () => {
            window.removeEventListener("resize", handleResize)
        }
    }, [myRef])

    return dimensions;
}