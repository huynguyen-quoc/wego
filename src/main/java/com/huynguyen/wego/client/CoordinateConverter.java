package com.huynguyen.wego.client;

import com.huynguyen.wego.client.dto.CoordinateResponse;

public interface CoordinateConverter {
    CoordinateResponse convert(String longitude, String latitude);
}
