import React, { useEffect, useRef } from 'react';
import {
    View,
    Text,
    TouchableOpacity,
    StyleSheet,
    Animated,
} from 'react-native';
import { useOrientation } from '../hooks/useOrientation';

interface FloatingSnackbarProps {
    message: string;
    visible: boolean;
    onDismiss: () => void;
    duration?: number;
    position?: 'top' | 'bottom';
}

const FloatingSnackbar: React.FC<FloatingSnackbarProps> = ({
    message,
    visible,
    onDismiss,
    duration = 4000,
    position = 'bottom',
}) => {
    const fadeAnim = useRef(new Animated.Value(0)).current;
    const slideAnim = useRef(new Animated.Value(50)).current;
    const { isLandscape, width, height } = useOrientation();

    // Debug log
    console.log('FloatingSnackbar orientation:', { isLandscape, width, height });

    useEffect(() => {
        if (visible) {
            Animated.parallel([
                Animated.timing(fadeAnim, {
                    toValue: 1,
                    duration: 300,
                    useNativeDriver: true,
                }),
                Animated.timing(slideAnim, {
                    toValue: 0,
                    duration: 300,
                    useNativeDriver: true,
                }),
            ]).start();

            const timer = setTimeout(() => {
                onDismiss();
            }, duration);

            return () => clearTimeout(timer);
        } else {
            Animated.parallel([
                Animated.timing(fadeAnim, {
                    toValue: 0,
                    duration: 300,
                    useNativeDriver: true,
                }),
                Animated.timing(slideAnim, {
                    toValue: 50,
                    duration: 300,
                    useNativeDriver: true,
                }),
            ]).start();
        }
    }, [visible, fadeAnim, slideAnim, duration]);

    if (!visible) return null;

    // Calculate positioning based on orientation
    const getContainerStyle = () => {
        const baseStyle = [
            styles.container,
            position === 'top' ? styles.top : styles.bottom,
        ];

        if (isLandscape) {
            // In landscape, make snackbar more compact and positioned differently
            return [
                ...baseStyle,
                styles.landscapeContainer,
                position === 'top' ? styles.landscapeTop : styles.landscapeBottom,
            ];
        }

        return baseStyle;
    };

    const getSnackbarStyle = () => {
        const baseStyle = [styles.snackbar];

        if (isLandscape) {
            return [...baseStyle, styles.landscapeSnackbar];
        }

        return baseStyle;
    };

    return (
        <Animated.View
            key={`snackbar-${isLandscape}`} // Force re-render on orientation change
            style={[
                ...getContainerStyle(),
                {
                    opacity: fadeAnim,
                    transform: [
                        {
                            translateY: position === 'top' ?
                                slideAnim.interpolate({
                                    inputRange: [0, 50],
                                    outputRange: [0, -50],
                                }) : slideAnim
                        }
                    ],
                },
            ]}
        >
            <View style={getSnackbarStyle()}>
                <Text style={[styles.message, isLandscape && styles.landscapeMessage]} numberOfLines={isLandscape ? 1 : 2}>
                    {message}
                </Text>
                <TouchableOpacity onPress={onDismiss} style={[styles.dismissButton, isLandscape && styles.landscapeDismissButton]}>
                    <Text style={styles.dismissText}>Dismiss</Text>
                </TouchableOpacity>
            </View>
        </Animated.View>
    );
};

const styles = StyleSheet.create({
    container: {
        position: 'absolute',
        left: 16,
        right: 16,
        zIndex: 1000,
    },
    top: {
        top: 100,
    },
    bottom: {
        bottom: 100,
    },
    landscapeContainer: {
        left: 32,
        right: 32,
    },
    landscapeTop: {
        top: 80, // Closer to top in landscape
    },
    landscapeBottom: {
        bottom: 60, // Closer to bottom in landscape
    },
    snackbar: {
        flexDirection: 'row',
        alignItems: 'center',
        backgroundColor: '#323232',
        paddingHorizontal: 16,
        paddingVertical: 12,
        borderRadius: 8,
        elevation: 6,
        shadowColor: '#000',
        shadowOffset: { width: 0, height: 4 },
        shadowOpacity: 0.3,
        shadowRadius: 6,
    },
    landscapeSnackbar: {
        paddingHorizontal: 12,
        paddingVertical: 8,
        borderRadius: 6,
    },
    message: {
        flex: 1,
        color: '#fff',
        fontSize: 14,
    },
    landscapeMessage: {
        fontSize: 13, // Smaller text in landscape
    },
    dismissButton: {
        marginLeft: 12,
        paddingHorizontal: 8,
        paddingVertical: 4,
    },
    landscapeDismissButton: {
        marginLeft: 8,
        paddingHorizontal: 6,
        paddingVertical: 2,
    },
    dismissText: {
        color: '#90CAF9',
        fontSize: 14,
        fontWeight: '600',
    },
});

export default FloatingSnackbar;