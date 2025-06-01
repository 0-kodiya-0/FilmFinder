import React, { useEffect, useRef } from 'react';
import {
    View,
    Text,
    TouchableOpacity,
    StyleSheet,
    Animated,
    Dimensions
} from 'react-native';

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
    }, [visible, fadeAnim, slideAnim, duration, onDismiss]);

    if (!visible) return null;

    return (
        <Animated.View
            style={[
                styles.container,
                position === 'top' ? styles.top : styles.bottom,
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
            <View style={styles.snackbar}>
                <Text style={styles.message} numberOfLines={2}>
                    {message}
                </Text>
                <TouchableOpacity onPress={onDismiss} style={styles.dismissButton}>
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
    message: {
        flex: 1,
        color: '#fff',
        fontSize: 14,
    },
    dismissButton: {
        marginLeft: 12,
        paddingHorizontal: 8,
        paddingVertical: 4,
    },
    dismissText: {
        color: '#90CAF9',
        fontSize: 14,
        fontWeight: '600',
    },
});

export default FloatingSnackbar;