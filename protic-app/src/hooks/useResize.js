import { useState, useEffect } from 'react';

export const useResize = (myRef) => {

    const [dimensions, setDimensions] = useState({ width: 0, height: 0 })
    const [position, setPosition] = useState({ left: 0, top: 0 })

    useEffect(() => {
        const handleResize = () => {
            setDimensions({
                ...dimensions,
                width: myRef.current.offsetWidth,
                height: myRef.current.offsetHeight
            })
        }

        const handlePosition = () => {
            setPosition({...position, 
                top: myRef.current.offsetHeight + myRef.current.getBoundingClientRect().top })
 
        }

        if (myRef.current) {
            setDimensions({
                ...dimensions,
                width: myRef.current.offsetWidth,
                height: myRef.current.offsetHeight
            })
            setPosition({...position, 
                top: myRef.current.offsetHeight + myRef.current.getBoundingClientRect().top })
        }

        window.addEventListener("resize", handleResize)
        window.addEventListener("scroll", handlePosition)

        return () => {
            window.removeEventListener("resize", handleResize);
            window.removeEventListener("scroll", handlePosition);
        }
    }, [myRef])

    return [dimensions, position];
}